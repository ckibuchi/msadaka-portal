/*<![CDATA[*/
function submitPayBill(){

    $(".loading").show();
   /* var dob = Date.parse($("#doB").val());
    var joining = Date.parse($("#joiningDate").val());
    var formData={
        phone: $("#phone").val(),
        user : {
            username : $("#username").val(),
            email : $("#email").val(),
            password:$("#password").val(),
            firstName:$("#firstName").val(),
            lastName:$("#lastName").val(),
            surName:$("#surName").val(),
            doB:dob.toString('yyyy-MM-dd'),
            gender:$("#gender").val(),
            joiningDate:joining.toString('yyyy-MM-dd')
        }
    }*/
	 
	console.log('assests -- '+ConvertFormToJSON($("#PayBillForm") ));

    $.ajax({
        url : "/newPayBill",
        type : 'POST', 
        data :JSON.stringify($("#PayBillForm").serializeArray()), //JSON.stringify(formData),//
        contentType : "application/json",
        success : function(data, textStatus, jqXHR) {
 
            $(".loading").hide();
        },
        error: function(jqXHR, textStatus, errorThrown){
            $(".loading").hide();
            console.log(jqXHR.responseJSON.error);
            alert(jqXHR.responseJSON.status+' :' +jqXHR.responseJSON.error);
        }
    });
}


function ConvertFormToJSON(form){
    var array = jQuery(form).serializeArray();
    var json = {};
    
    jQuery.each(array, function() {
        json[this.name] = this.value || '';
    });
    
    return json;
}
/*]]>*/