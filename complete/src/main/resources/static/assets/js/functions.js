/*<![CDATA[*/
function submitPayBill(){

    $(".loading").show();

    var formData={
        id: $("#id").val(),
        name: $("#name").val(),
        altName : $("#name").val(),
        mpesaPasskey : $("#mpesaPasskey").val(),
        shortCode : $("#shortCode").val(),
        payBill:$("#payBill").val()
    }

    $.ajax({
        url : "/newPayBill",
        type : 'POST',
        data :JSON.stringify(formData),//
        contentType : "application/json",
        success : function(data, status, jqXHR) {
            $(".loading").hide();
            console.log('Response: '+data.toString());
             /*   $("#paybills tbody").append(
                    "<tr>"
                    + "<td>" +($('#paybills tr').length+1) +"</td>"
                    + "<td>" + $("#shortCode").val() + "</td>"
                    + "<td>" + $("#payBill").val() + "</td>"
                    + "<td>" + $("#name").val() + "</td>"
                    + "<td>" + "</td>"
                    + "<td>" + "</td>"
                    + "<td><span class='badge badge-info'>Active</span></td>"
                    +"<td></td>"
                    + "</tr>");*/
            $("#create-paybill-modal").modal("hide");
            $('#PayBillForm')[0].reset();

            location.reload();



        },
        error: function(jqXHR, status, errorThrown){
           $(".loading").hide();

            alert(jqXHR.responseJSON.status+' :' +jqXHR.responseJSON.error);
            console.log(jqXHR.responseJSON.error);
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
/*]]>*/

function editPayBill(id,name,mpesaPasskey,shortCode,payBill) {


    $('#id').val(id);
    $('#name').val(name);
    $('#mpesaPasskey').val(mpesaPasskey);
    $('#shortCode').val(shortCode);
    $('#payBill').val(payBill);

    $('#create-paybill-modal').modal('show');
}

function showdeletePayBill(id,name,mpesaPasskey,shortCode,payBill) {
    $(".del_loading").hide();
    $('#payBillId').val(id);
    $('#question').text('Are you sure want to delete '+name+', '+payBill+'?');


    $('#delete_paybill').modal('show');
}
function  deletePayBill()
{

    $(".del_loading").show();

    var formData={
        id:$("#payBillId").val()

    }

    $.ajax({
        url : "/deletePayBill",
        type : 'POST',
        data :JSON.stringify(formData),//
        contentType : "application/json",
        success : function(data, status, jqXHR) {
            $(".del_loading").hide();
            console.log('Response: '+data);
            /*   $("#paybills tbody").append(
                   "<tr>"
                   + "<td>" +($('#paybills tr').length+1) +"</td>"
                   + "<td>" + $("#shortCode").val() + "</td>"
                   + "<td>" + $("#payBill").val() + "</td>"
                   + "<td>" + $("#name").val() + "</td>"
                   + "<td>" + "</td>"
                   + "<td>" + "</td>"
                   + "<td><span class='badge badge-info'>Active</span></td>"
                   +"<td></td>"
                   + "</tr>");*/

            var res= JSON.parse(data);////JSON.stringify(data);

            if(res.status=='success') {

                $("#delete_paybill").modal("hide");
                $('#deletePayBill')[0].reset();
                location.reload();
            }
            else {
                alert(res.status+' :' +res.message);
            }




        },
        error: function(jqXHR, status, errorThrown){
            $(".del_loading").hide();
            console.log(jqXHR.responseJSON.error);
            alert(jqXHR.responseJSON.status+' :' +jqXHR.responseJSON.error);
        }
    });

}

function newPayBill() {
    $('#PayBillForm')[0].reset();
    $('#create-paybill-modal').modal('show');
}
