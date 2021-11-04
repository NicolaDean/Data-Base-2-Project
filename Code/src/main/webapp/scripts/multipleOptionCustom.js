
//Modify tag of select
function multiple_custom(caller,customTag,id)
{
    var option = document.getElementById(customTag+"-"+id);
    option.selected = !option.selected;
    if(option.selected)
    {
        caller.className = "card text-white bg-secondary mb-3";
    }
    else{
        caller.className = "card border-secondary mb-3";
    }
}
/*
function multipleOptionCustom(selectorId,elemetsTag)
{
    this.options  = new Array();
    this.selector = options = document.querySelectorAll("#multiple-products");

    this.start = function()
    {
        var i=0;
        var self = this;
        this.selector.forEach((elem) =>{
            //Add option to data structure list
            self.options.push(elem);
            console.log("create "+ i);
            console.log(self.options[i]);
            var choice = document.getElementById("choice-"+i);

            self.options
            var self2 = this;
            choice.addEventListener("click", e=>{
                console.log("Click Option" + i);
                //self2.options[i].selected = !self2.options[i].selected;
                console.log(self2.options(i).select);
            });
            i++;
        })
    }




}*/


