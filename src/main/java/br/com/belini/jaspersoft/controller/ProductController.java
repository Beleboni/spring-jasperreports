package br.com.belini.jaspersoft.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private DataSource dataSource;
	
	@GetMapping("/report")
	public void printReport(HttpServletResponse response) throws JRException, IOException, SQLException {
		
		InputStream reportStream = this.getClass().getResourceAsStream("/reports/Products.jasper");
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("PARAM_1", "CUSTOM PARAM");
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=products_son.pdf");
		
		OutputStream outputStream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}

}