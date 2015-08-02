package org.easystogu.network;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumDriverHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
		capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		WebDriver driver = new InternetExplorerDriver(capability);
		driver.get("http://data.eastmoney.com/zjlx/detail.html");
		System.out.println("title=" + driver.getTitle());
		WebElement gopageInput = driver.findElement(By.id("gopage"));
		gopageInput.clear();
		gopageInput.sendKeys("2");
		WebElement goPageSubmit = driver.findElement(By.className("btn_link"));
		goPageSubmit.submit();
		System.out.println("title=" + driver.getTitle());
		driver.quit();
	}
}
