package services;

import java.util.Locale;

public class ChangeLang {

    private Locale locale;

    public ChangeLang() {
        // Set a default locale
        locale = new Locale("eng");
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(String lang) {
        locale = new Locale(lang);

        //FacesContext context = FacesContext.getCurrentInstance();
        //UIViewRoot viewRoot = context.getViewRoot();
        //viewRoot.setLocale(locale);

        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Locale changed to " + lang, null);
        //context.addMessage(null, message);
    }


}
