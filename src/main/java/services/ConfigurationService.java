
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

	private Object					cont;


	public Configuration getConfiguration() {
		return this.configurationRepository.findAll().get(0);
	}

	public Configuration save(final Configuration configuration) {
		return this.configurationRepository.save(configuration);
	}

	public List<String> getSpamWords() {
		return this.configurationRepository.spamWords();
	}

	public Boolean isStringSpam(final String s, final List<String> spamWords) {
		Boolean result = false;

		List<String> trimmedString = new ArrayList<String>();
		trimmedString = Arrays.asList(s.split("\\+|(?=[,.¿?;!¡])"));

		// ("\\s*(=>|,|\\s)\\s*"));
		for (final String g : spamWords) {
			for (final String c : trimmedString) {
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
		Integer messagesCount = 0;
		Double spamPorcent = 0.;

		// COMPROBANDO LAS CAJAS DEL ACTOR

		for (Message g : a.getMessages()) {
			if (g.getSender().equals(a) && (this.isStringSpam(g.getBody(), spamWords) || this.isStringSpam(g.getSubject(), spamWords))) {
				spamCount++;
			}
		}

		spamPorcent = spamCount / messagesCount * 100.;

		if (spamPorcent >= 10) {
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
		final String goodWordString = this.configurationRepository.goodWords();

		final List<String> goodWordsList = Arrays.asList(goodWordString.split(",[ ]*"));

		return goodWordsList;
	}

	public List<String> showBadWordsList() {
		this.adminService.loggedAsAdmin();
		final String badWordString = this.configurationRepository.badWords();

		final List<String> badWordsList = Arrays.asList(badWordString.split(",[ ]*"));

		return badWordsList;
	}

	public String addGoodWords(final String word) {
		this.adminService.loggedAsAdmin();
		final Configuration configuration = this.configurationRepository.configuration();
		String goodWords = configuration.getGoodWords();
		configuration.setGoodWords(goodWords = goodWords + "," + word);
		this.configurationRepository.save(configuration);

		return configuration.getGoodWords();
	}

	public String addBadWords(final String word) {
		this.adminService.loggedAsAdmin();
		final Configuration configuration = this.configurationRepository.configuration();
		String badWords = configuration.getBadWords();
		configuration.setBadWords(badWords = badWords + "," + word);
		this.configurationRepository.save(configuration);

		return configuration.getBadWords();
	}

	public String editWord(final String word, final String originalWord) {
		this.adminService.loggedAsAdmin();
		String result = "";
		final String goodWords = this.showGoodWords();
		final String badWords = this.showBadWords();
		final Configuration configuration = this.configurationRepository.configuration();
		final List<String> goodWordsList = Arrays.asList(goodWords.split(",[ ]*"));
		final List<String> badWordsList = Arrays.asList(badWords.split(",[ ]*"));

		Integer cont = 0;

		if (goodWordsList.contains(originalWord)) {

			for (final String s : goodWordsList) {
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
			for (final String s : badWordsList) {
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

	public void deleteGoodWord(final String word) {
		this.adminService.loggedAsAdmin();
		final String goodWords = this.showGoodWords();
		final Configuration configuration = this.configurationRepository.configuration();

		final List<String> goodWordsList = new ArrayList<String>();
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

	public void deleteBadWord(final String word) {
		this.adminService.loggedAsAdmin();
		final String badWords = this.showBadWords();
		final Configuration configuration = this.configurationRepository.configuration();

		final List<String> badWordsList = new ArrayList<String>();
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
