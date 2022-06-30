//  an https://stackoverflow.com/questions/69909255/how-to-bind-a-dynamic-child-list-using-thymeleaf-and-jquery angelehnt
    $(document).ready(function () {
    let max_fields = 30;
    let index = 1;


    $('.add-exercise').click(function () {

        if (index < max_fields) {
            let newRow = jQuery('<div class="exercise row mb-5">' +
                ' <div class="mb-3">' +
                ' Exercise description*:' +
                ' <input name="exercises[' + index + '].description" type="text" class="form-control" maxlength="150"/>' +
                ' </div>' +
                ' <div class="mb-3">' +
                '  Weight(in kg):' +
                ' <input name="exercises[' + index + '].weight" type="number" step="1" min="0" maxlength="3"' +
                ' oninput="if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"' +
                ' class="form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Reps:' +
                ' <input name="exercises[' + index + '].sets" type="number" step="1" min="0" maxlength="2"' +
                ' oninput="if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"' +
                ' class="form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Sets:' +
                ' <input name="exercises[' + index + '].reps" type="number" step="1" min="0" maxlength="2"' +
                ' oninput="if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);"' +
                ' class="form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                ' Additional note:' +
                ' <input name="exercises[' + index + '].note" type="text" class="form-control" maxlength="200"/> ' +
                ' </div>'+
                '</div>');

            index++;

            $('.exercises').append(newRow)
        }

    });
});