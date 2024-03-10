package Beans;

import jakarta.annotation.ManagedBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@ManagedBean
@SessionScoped
public class ChangeLang implements Serializable {

    private static final long serialVersionUID = 1L;

    private String localeCode;

    private static Map<String, Object> countries;

    static {
        countries = new LinkedHashMap<>();
        // On recupere la langue du navigateur
        String browserLanguage = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
        // On cree d'abord une entree pour la langue du navigateur
        countries.put(getLanguageName(browserLanguage), new Locale(browserLanguage));
        // Ensuite, on ajoute les autres langues
        countries.put("English", Locale.ENGLISH);
        countries.put("Français", Locale.FRENCH);
        countries.put("Italiano", Locale.ITALIAN);

    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocaleCode() {
        if (localeCode == null) {
            // Par defaut, definir la langue sur la langue du navigateur la premiere fois que la page est visitee
            localeCode = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale().getLanguage();
        }
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        String newLocaleValue = e.getNewValue().toString();

        for (Map.Entry<String, Object> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
            }
        }
    }

    // Methode utilitaire pour obtenir le nom de la langue à partir du code de langue
    private static String getLanguageName(String languageCode) {
        switch (languageCode) {
            case "en":
                return "English";
            case "fr":
                return "Français";
            case "it":
                return "Italiano";
            default:
                return "Other";
        }
    }
}
