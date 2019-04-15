
package controllers;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import services.CurriculumService;
import services.HackerService;
import domain.Hacker;

@Controller
@RequestMapping("/export")
public class ExportDataController {

	@Autowired
	HackerService		hackerService;

	@Autowired
	CurriculumService	curriculumService;


	@RequestMapping(value = "/hacker", method = RequestMethod.GET)
	public @ResponseBody
	String export(@RequestParam(value = "id", defaultValue = "-1") int id, HttpServletResponse response) throws IOException {

		this.hackerService.loggedAsHacker();

		Hacker hacker = new Hacker();
		hacker = this.hackerService.findOne(id);

		// Defines un StringBuilder para construir tu string
		StringBuilder sb = new StringBuilder();

		// linea
		sb.append("Personal data:").append(System.getProperty("line.separator"));
		sb.append("Name: " + hacker.getName()).append(System.getProperty("line.separator"));
		sb.append("Surname: " + hacker.getSurname()).append(System.getProperty("line.separator"));
		sb.append("Address: " + hacker.getAddress()).append(System.getProperty("line.separator"));
		sb.append("Email: " + hacker.getEmail()).append(System.getProperty("line.separator"));
		sb.append("Photo: " + hacker.getPhoto()).append(System.getProperty("line.separator"));
		sb.append("VAT number: " + hacker.getVATNumber()).append(System.getProperty("line.separator"));

		sb.append(System.getProperty("line.separator"));
		sb.append("SocialProfiles: ").append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		// Este metodo te muestra los socialProfiles de la misma manera que el resto del
		// documento
		sb.append(this.hackerService.SocialProfilesToString()).append(System.getProperty("line.separator"));

		sb.append(System.getProperty("line.separator"));
		sb.append("Curriculums: ").append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.curriculumService.curriculumToStringExport()).append(System.getProperty("line.separator"));

		if (hacker == null || this.hackerService.loggedHacker().getId() != id)
			return null;

		// Defines el nombre del archivo y la extension
		response.setContentType("text/txt");
		response.setHeader("Content-Disposition", "attachment;filename=exportDataHacker.txt");

		// Con estos comandos permites su descarga cuando clickas
		ServletOutputStream outStream = response.getOutputStream();
		outStream.println(sb.toString());
		outStream.flush();
		outStream.close();

		// El return no llega nunca, es del metodo viejo
		return sb.toString();
	}
}
