
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConfigurationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Configuration;
import domain.Message;

@Service
@Transactional
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository	configurationRepository;

	@Autowired
	private AdminService			adminService;

	@Autowired
	private Validator				validator;


	public Configuration getConfiguration() {
		return this.configurationRepository.findAll().get(0);
	}

	public Configuration save(Configuration configuration) {
		return this.configurationRepository.save(configuration);
	}

	public List<String> getSpamWords() {
		return this.configurationRepository.spamWords();
	}

	public Boolean isStringSpam(String s, List<String> spamWords) {
		Boolean result = false;

		List<String> trimmedString = new ArrayList<String>();
		trimmedString = Arrays.asList(s.split("\\+|(?=[,.¿?;!¡])"));

		// ("\\s*(=>|,|\\s)\\s*"));
		for (String g : spamWords) {
			for (String c : trimmedString) {
				if (g.equals(c) || g.equalsIgnoreCase(c)) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

	public Boolean isActorSuspicious(Actor a) {
		boolean result = false;
		List<String> spamWords = new ArrayList<String>();
		spamWords = this.getSpamWords();
		Integer spamCount = 0;

		List<Message> messages = this.configurationRepository.messagesSendedByActor(a.getId());
		Integer messagesCount = messages.size();

		// COMPROBANDO LAS CAJAS DEL ACTOR

		for (Message g : messages) {
			if (this.isStringSpam(g.getBody(), spamWords) || this.isStringSpam(g.getSubject(), spamWords) || this.isStringSpam(g.getTags(), spamWords)) {
				spamCount++;
			}
		}

		if (spamCount * 10 >= messagesCount) {
			result = true;
		}

		return result;
	}

	public String showGoodWords() {
		return this.configurationRepository.goodWords();
	}

	public String showBadWords() {
		return this.configurationRepository.badWords();
	}

	public List<String> showGoodWordsList() {
		this.adminService.loggedAsAdmin();
		String goodWordString = this.configurationRepository.goodWords();

		List<String> goodWordsList = Arrays.asList(goodWordString.split(",[ ]*"));

		return goodWordsList;
	}

	public List<String> showBadWordsList() {
		this.adminService.loggedAsAdmin();
		String badWordString = this.configurationRepository.badWords();

		List<String> badWordsList = Arrays.asList(badWordString.split(",[ ]*"));

		return badWordsList;
	}

	public String addGoodWords(String word) {
		this.adminService.loggedAsAdmin();
		Configuration configuration = this.configurationRepository.configuration();
		String goodWords = configuration.getGoodWords();
		configuration.setGoodWords(goodWords = goodWords + "," + word);
		this.configurationRepository.save(configuration);

		return configuration.getGoodWords();
	}

	public String addBadWords(String word) {
		this.adminService.loggedAsAdmin();
		Configuration configuration = this.configurationRepository.configuration();
		String badWords = configuration.getBadWords();
		configuration.setBadWords(badWords = badWords + "," + word);
		this.configurationRepository.save(configuration);

		return configuration.getBadWords();
	}

	public String editWord(String word, String originalWord) {
		this.adminService.loggedAsAdmin();
		String result = "";
		String goodWords = this.showGoodWords();
		String badWords = this.showBadWords();
		Configuration configuration = this.configurationRepository.configuration();
		List<String> goodWordsList = Arrays.asList(goodWords.split(",[ ]*"));
		List<String> badWordsList = Arrays.asList(badWords.split(",[ ]*"));

		Integer cont = 0;

		if (goodWordsList.contains(originalWord)) {

			for (String s : goodWordsList) {
				if (s.equals(originalWord)) {
					goodWordsList.set(cont, word);
				}
				cont++;
			}

			for (int i = 0; i < goodWordsList.size(); i++) {
				if (i < goodWordsList.size() - 1) {
					result = result + goodWordsList.get(i) + ",";
				} else {
					result = result + goodWordsList.get(i);
				}
			}
			configuration.setGoodWords(result);

		} else {
			for (String s : badWordsList) {
				if (s.equals(originalWord)) {
					badWordsList.set(cont, word);
				}
				cont++;
			}

			for (int i = 0; i < badWordsList.size(); i++) {
				if (i < badWordsList.size() - 1) {
					result = result + badWordsList.get(i) + ",";
				} else {
					result = result + badWordsList.get(i);
				}
			}
			configuration.setBadWords(result);
		}

		this.configurationRepository.save(configuration);

		return configuration.getGoodWords();
	}

	public void deleteGoodWord(String word) {
		this.adminService.loggedAsAdmin();
		String goodWords = this.showGoodWords();
		Configuration configuration = this.configurationRepository.configuration();

		List<String> goodWordsList = new ArrayList<String>();
		goodWordsList.addAll(Arrays.asList(goodWords.split(",[ ]*")));

		if (goodWordsList.contains(word)) {
			goodWordsList.remove(word);
		}

		String result = "";

		for (int i = 0; i < goodWordsList.size(); i++) {
			if (i < goodWordsList.size() - 1) {
				result = result + goodWordsList.get(i) + ",";
			} else {
				result = result + goodWordsList.get(i);
			}
		}

		configuration.setGoodWords(result);
		this.configurationRepository.save(configuration);
	}

	public void deleteBadWord(String word) {
		this.adminService.loggedAsAdmin();
		String badWords = this.showBadWords();
		Configuration configuration = this.configurationRepository.configuration();

		List<String> badWordsList = new ArrayList<String>();
		badWordsList.addAll(Arrays.asList(badWords.split(",[ ]*")));

		if (badWordsList.contains(word)) {
			badWordsList.remove(word);
		}

		String result = "";

		for (int i = 0; i < badWordsList.size(); i++) {
			if (i < badWordsList.size() - 1) {
				result = result + badWordsList.get(i) + ",";
			} else {
				result = result + badWordsList.get(i);
			}
		}

		configuration.setBadWords(result);
		this.configurationRepository.save(configuration);
	}

	public Configuration reconstruct(Configuration configuration, BindingResult binding) {
		this.loggedAsAdmin();

		Configuration result = new Configuration();

		Configuration configurationBefore = this.configurationRepository.findOne(configuration.getId());

		result.setId(configurationBefore.getId());
		result.setVersion(configurationBefore.getVersion());
		result.setBadWords(configurationBefore.getBadWords());
		result.setGoodWords(configurationBefore.getGoodWords());

		result.setFinderResult(configuration.getFinderResult());
		result.setMinFinderResults(configuration.getMinFinderResults());
		result.setmaxFinderResults(configuration.getmaxFinderResults());
		result.setTimeFinder(configuration.getTimeFinder());
		result.setSpainTelephoneCode(configuration.getSpainTelephoneCode());
		result.setWelcomeMessageEnglish(configuration.getWelcomeMessageEnglish());
		result.setWelcomeMessageSpanish(configuration.getWelcomeMessageSpanish());
		result.setSystemName(configuration.getSystemName());
		result.setImageURL(configuration.getImageURL());
		result.setMaxTimeFinder(configuration.getMaxTimeFinder());
		result.setMinTimeFinder(configuration.getMinTimeFinder());
		result.setVAT(configuration.getVAT());
		result.setFare(configuration.getFare());
		result.setCardType(configuration.getCardType());

		this.validator.validate(result, binding);

		return result;

	}

	public void loggedAsAdmin() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("ADMIN"));
	}

}
