
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Application;
import domain.Message;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ApplicationService applicationService;

	public Message findOne(int messageId) {
		return this.messageRepository.findOne(messageId);
	}

	public List<Message> findAll() {
		return this.messageRepository.findAll();

	}

	public Message save(Message message) {
		return this.messageRepository.save(message);
	}

	public void delete(Message message) {
		this.messageRepository.delete(message);
	}

	public void flush() {
		this.messageRepository.flush();
	}

	public void deleteInbatch(List<Message> messages) {
		this.messageRepository.deleteInBatch(messages);
	}

	public List<Message> showMessages() {
		this.actorService.loggedAsActor();
		Actor actor = new Actor();
		actor = this.actorService.loggedActor();
		List<Message> messagesOfActor = new ArrayList<Message>();
		messagesOfActor = this.messageRepository.messagesOfLoggedActor(actor);

		return messagesOfActor;
	}

	public Message create() {
		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1000);

		Message message = new Message();

		message.setMoment(thisMoment);
		message.setSubject("");
		message.setBody("");
		message.setReceiver("");
		message.setTags("");
		message.setSender(userAccount.getUsername());

		return message;
	}

	public Message createCopy(String Subject, String body, String tags, String recipient) {

		this.actorService.loggedAsActor();

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		Message message = new Message();

		message.setMoment(thisMoment);
		message.setSubject(Subject);
		message.setBody(body);
		message.setReceiver(recipient);
		message.setTags(tags);
		message.setSender(userAccount.getUsername());

		return message;
	}

	public Message create(String Subject, String body, String tags, String sender, String receiver) {

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		Message message = new Message();

		message.setMoment(thisMoment);
		message.setSubject(Subject);
		message.setBody(body);
		message.setTags(tags);
		message.setSender(sender);
		message.setReceiver(receiver);

		return message;
	}

	public void deleteMessage(Message message) {
		Actor actor = this.actorService.loggedActor();
		Assert.isTrue(actor.getMessages().contains(message));

		if (message.getTags().contains("DELETED")) {
			actor.getMessages().remove(message);
			this.delete(message);
		} else {
			actor.getMessages().remove(message);
			message.setTags("DELETED");
			actor.getMessages().add(message);
		}
	}

	public Message sendMessage(Message message) {

		this.actorService.loggedAsActor();

		List<String> spam = new ArrayList<String>();
		spam = this.configurationService.getSpamWords();

		Actor loggedActor = this.actorService.loggedActor();

		Actor receiver = this.actorService.getActorByUsername(message.getReceiver());

		List<String> spamWords = new ArrayList<String>();
		spamWords = this.configurationService.getSpamWords();

		Message messageSaved = this.messageRepository.save(message);
		Message messageCopy = this.createCopy(messageSaved.getSubject(), messageSaved.getBody(), message.getTags(),
				messageSaved.getReceiver());

		Boolean hasSpam = this.configurationService.isStringSpam(message.getBody(), spamWords)
				|| this.configurationService.isStringSpam(message.getSubject(), spamWords)
				|| this.configurationService.isStringSpam(message.getTags(), spamWords);

		if (hasSpam) {
			messageCopy.setTags("SPAM");
		}

		Message messageSavedCopy = this.messageRepository.save(messageCopy);

		loggedActor.getMessages().add(messageSaved);
		this.actorService.save(loggedActor);

		receiver.getMessages().add(messageSavedCopy);
		this.actorService.save(receiver);

		this.flush();
		if (hasSpam) {
			this.actorService.updateActorSpam(loggedActor);
		}

		return messageSaved;
	}

	public void sendMessageWithActors(Message message, Actor loggedActor, Actor receiver) {

		this.actorService.loggedAsActor();

		List<String> spam = new ArrayList<String>();
		spam = this.configurationService.getSpamWords();

		List<String> spamWords = new ArrayList<String>();
		spamWords = this.configurationService.getSpamWords();

		Message messageSaved = this.messageRepository.save(message);
		Message messageCopy = this.createCopy(messageSaved.getSubject(), messageSaved.getBody(), message.getTags(),
				messageSaved.getReceiver());

		Boolean hasSpam = this.configurationService.isStringSpam(message.getBody(), spamWords)
				|| this.configurationService.isStringSpam(message.getSubject(), spamWords)
				|| this.configurationService.isStringSpam(message.getTags(), spamWords);

		if (hasSpam) {
			messageCopy.setTags("SPAM");
		}

		Message messageSavedCopy = this.messageRepository.save(messageCopy);

		loggedActor.getMessages().add(messageSaved);
		this.actorService.save(loggedActor);

		receiver.getMessages().add(messageSavedCopy);
		this.actorService.save(receiver);

		this.flush();
		if (hasSpam) {
			this.actorService.updateActorSpam(loggedActor);
		}
	}

	public void notificationStatusApplicationSubmitted(Application app) {

		Actor sender = app.getHacker();
		Actor receiver = this.applicationService.getCompanyByApplicationId(app.getId());

		Message company = this.create("Status change",
				"The application " + app.getProblem().getTitle() + " has changed its status to submitted by the hacker "
						+ sender.getName() + ". / La aplicacion " + app.getProblem().getTitle()
						+ " ha cambiado su estado a entregada por el hacker " + sender.getName() + ".",
				"Notificacion / Notification", sender.getName(), receiver.getName());

		this.sendMessageWithActors(company, sender, receiver);
	}
	
	public void notificationStatusApplicationAccepted(Application app) {

		Actor sender = app.getHacker();
		Actor receiver = this.applicationService.getCompanyByApplicationId(app.getId());

		Message company = this.create("Status change",
				"The application " + app.getProblem().getTitle() + " has changed its status to accepted by the company "
						+ receiver.getName() + ". / La aplicacion " + app.getProblem().getTitle()
						+ " ha cambiado su estado a aceptada por la compania " + receiver.getName() + ".",
				"Notificacion / Notification", receiver.getName(), sender.getName());

		this.sendMessageWithActors(company, receiver, sender);
	}
	
	public void notificationStatusApplicationRejected(Application app) {

		Actor sender = app.getHacker();
		Actor receiver = this.applicationService.getCompanyByApplicationId(app.getId());

		Message company = this.create("Status change",
				"The application " + app.getProblem().getTitle() + " has changed its status to rejected by the company "
						+ receiver.getName() + ". / La aplicacion " + app.getProblem().getTitle()
						+ " ha cambiado su estado a rechazada por la compania " + receiver.getName() + ".",
				"Notificacion / Notification", receiver.getName(), sender.getName());

		this.sendMessageWithActors(company, receiver, sender);
	}

	public Message reconstruct(Message messageTest, BindingResult binding) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Message result;

		if (messageTest.getId() == 0) {
			result = messageTest;
			result.setSender(userAccount.getUsername());
			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1000);
			result.setMoment(thisMoment);

		} else {
			result = this.messageRepository.findOne(messageTest.getId());

			result.setSender(userAccount.getUsername());

			result.setBody(messageTest.getBody());
			result.setTags(messageTest.getTags());
			result.setSubject(messageTest.getSubject());
			result.setReceiver(messageTest.getReceiver());

		}

		this.validator.validate(result, binding);
		return result;

	}

}
