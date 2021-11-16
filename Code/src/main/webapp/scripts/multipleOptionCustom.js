
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


