function addService()
{
    var form            = document.getElementById("packageForm");
    var container       = document.getElementById("services-container");

    var serviceForm     = document.createElement("div");
        serviceForm.id  = selectedType;
        serviceForm.className = "form-group";

    var selectedType    = form.elements["service-type"].value;

    console.log("Added service" + selectedType);

    switch (selectedType){
        case 'MIS':
            addInternetForm(serviceForm,"MIS");
            break;
        case 'FIS':
            addInternetForm(serviceForm,"FIS");
            break;
        case 'MPS':
            addMobilePhoneForm(serviceForm);
            break;
        case 'FPS':
            addFixedPhoneForm(serviceForm);
            break;
    }

    container.appendChild(serviceForm);
}

function addInternetForm(serviceForm,type)
{
    console.log(serviceForm);
    serviceForm.innerHTML =
        "<div class=\"card border-secondary mb-3\" style=\"max-width: 32rem;\">\n" +
        "<div class=\"card-header\">Internet Phone Services</div>\n" +
        "<div class=\"card-body text-secondary\">\n" +
        "<div class=\"form-row\">\n" +
        "      <input hidden type=\"text\" name=\"internet-type\" value=\""+ type+"\">\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "      <label for=\"inputEmail4\">GB</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"Gb\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"extraGb\" min=\"0.01\" step=\"0.01\">\n" +
        "    </div>\n" +
        "    </div>\n" +
        "    </div>\n" +
        "    </div>\n";
}

function addMobilePhoneForm(serviceForm)
{
    console.log(serviceForm);
    serviceForm.innerHTML =
        "<div class=\"card border-secondary mb-3\" style=\"max-width: 32rem;\">\n" +
        "<div class=\"card-header\">Mobile Phone Services</div>\n" +
        "<div class=\"card-body text-secondary\">\n" +
        "<div class=\"form-row\">\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "      <label for=\"inputEmail4\">Minutes</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"Min\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "      <label for=\"inputEmail4\">SMS</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"Sms\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee for Min.</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"ExtraMin\" min=\"0.01\" step=\"0.01\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee for Sms.</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"ExtraSms\" min=\"0.01\" step=\"0.01\">\n" +
        "    </div>\n" +
        "  </div>\n"+
        "  </div>\n"+
        "  </div>\n";
}

function addFixedPhoneForm(serviceForm)
{
    //hidden form
    console.log(serviceForm);
    serviceForm.innerHTML = "<input hidden name=\"FPS\" value=\"1\"/>";
}

function addValidityPeriodForm()
{
    var container    = document.getElementById("validity-container");
    var validityForm = document.getElementById("validity-period");

    container.appendChild(validityForm.cloneNode(this));


}

function addOptionalProductForm()
{
    var container    = document.getElementById("optional-container");
    var validityForm = document.getElementById("optional-selection");

    container.appendChild(validityForm.cloneNode(this));
}

