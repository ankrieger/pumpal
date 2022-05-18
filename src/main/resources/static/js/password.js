// document.getElementById('showPassword').onclick = function() {
//     if ( this.checked ) {
//         document.getElementById('password').type = "text";
//     } else {
//         document.getElementById('password').type = "password";
//     }
// };

// https://formden.com/blog/date-picker
$(document).ready(function(){
    var date_input=$('input[name="birthDate"]');
    var options={
        format: 'dd.mm.yyyy',
        todayHighlight: false,
        autoclose: true,
    };
    date_input.datepicker(options);
})