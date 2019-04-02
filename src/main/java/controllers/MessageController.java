
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.ConfigurationService;
import services.MessageService;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		this.actorService.loggedAsActor();

		ModelAndView result;

		List<Message> messages = new ArrayList<Message>();

		messages = this.messageService.showMessages();

		result = new ModelAndView("message/actor/list");
		result.addObject("messages", messages);
		result.addObject("requestURI", "message/actor/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		this.actorService.loggedAsActor();
		ModelAndView result;
		Message message;

		message = this.messageService.create();
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("messageTest") domain.Message messageTest, BindingResult binding) {

		this.actorService.loggedAsActor();
		ModelAndView result;
		UserAccount userAccount = LoginService.getPrincipal();

		messageTest = this.messageService.reconstruct(messageTest, binding);

		Assert.isTrue(userAccount.getUsername().equals(messageTest.getSender()));

		if (binding.hasErrors())
			result = this.createEditModelAndView(messageTest);
		else
			try {
				this.messageService.sendMessage(messageTest);
				result = new ModelAndView("redirect: list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(messageTest, "message.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int rowId) {
		this.actorService.loggedAsActor();
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(rowId);

		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int rowId) {
		this.actorService.loggedAsActor();
		UserAccount userAccount = LoginService.getPrincipal();
		ModelAndView result;

		Message message = this.messageService.findOne(rowId);

		if (!(userAccount.getUsername().equals(message.getSender()) || userAccount.getUsername().equals(message.getReceiver())))
			return this.list();

		try {

			this.messageService.deleteMessage(message);
			result = new ModelAndView("redirect: list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(message, "message.commit.error");

		}
		return result;
	}

	protected ModelAndView createEditModelAndView(Message messageTest) {
		ModelAndView result;

		result = this.createEditModelAndView(messageTest, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Message messageTest, String messageCode) {
		ModelAndView result;

		List<String> actors = new ArrayList<String>();
		actors = this.actorService.usernamesOfActors();

		result = new ModelAndView("message/actor/create");
		result.addObject("messageTest", messageTest);
		result.addObject("actors", actors);

		result.addObject("message", messageCode);

		return result;
	}

}
