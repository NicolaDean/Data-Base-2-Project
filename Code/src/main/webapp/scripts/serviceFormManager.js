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
        case 'FIS':
            addInternetForm(serviceForm);
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

function addInternetForm(serviceForm)
{
    console.log(serviceForm);
    //use selectedType as id
    serviceForm.innerHTML = " <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"1\"><br><br> ";

    serviceForm.innerHTML =
        "<div class=\"card border-secondary mb-3\" style=\"max-width: 32rem;\">\n" +
        "<div class=\"card-header\">Mobile Phone Services</div>\n" +
        "<div class=\"card-body text-secondary\">\n" +
        "<div class=\"form-row\">\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "      <label for=\"inputEmail4\">GB</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"0.01\" step=\"0.01\">\n" +
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
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "      <label for=\"inputEmail4\">SMS</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"1\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee for Min.</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"0.01\" step=\"0.01\">\n" +
        "    </div>\n" +
        "    <div class=\"form-group col-md-6\">\n" +
        "     <label for=\"inputEmail4\">Extra fee for Sms.</label>\n" +
        "      <input type=\"number\" id=\"GB\" name=\"quantity\" min=\"0.01\" step=\"0.01\">\n" +
        "    </div>\n" +
        "  </div>\n"+
        "  </div>\n"+
        "  </div>\n";
}

function addFixedPhoneForm(serviceForm)
{
    //hidden form
    console.log(serviceForm);
    serviceForm.innerHTML = "<input id=\"FIS-service\"/>";
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

