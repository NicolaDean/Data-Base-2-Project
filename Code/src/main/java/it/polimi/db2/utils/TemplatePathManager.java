package it.polimi.db2.utils;

/**
 * Contains all the path to .html template loadable with thymeleaf
 */
public class TemplatePathManager {

    public static String root           = "/WEB-INF/templateHTML/";

    //Login and Registration
    public static String loginPage      = "index";
    public static String registration   = "registration";

    //TEMPLATES
    public static String homePage       = root + "home";
    public static String packageDetails = root + "packageDetails";
    public static String contract = "contract";
    public static String payment = "payment";
    public static String error = "errorPage";
}
