$(document).ready(function () {
    let max_fields = 30;
    let index = 1;


    $('.add-exercise').click(function () {

        if (index < max_fields) {
            let newRow = jQuery('<div class="exercise row">' +
                ' <div class="mb-3">' +
                ' Name' +
                ' <input name="exercises[' + index + '].name" type="text" class="form-control form-control-lg"/>' +
                ' </div>' +
                ' <div class="mb-3">' +
                '  Weight' +
                ' <input name="exercises[' + index + '].weight" type="number" step="1" min="0"' +
                ' class="form-control form-control-lg"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Sets' +
                ' <input name="exercises[' + index + '].sets" type="number" step="1" min="0"' +
                ' class="form-control form-control-lg"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Reps' +
                ' <input name="exercises[' + index + '].reps" type="number" step="1" min="0"' +
                ' class="form-control form-control-lg"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                ' Note' +
                ' <input name="exercises[' + index + '].note" type="text" class="form-control form-control-lg"/>' +
                ' </div>'+
                '</div>');

            index++;

            $('.exercises').append(newRow)
        }

    });
});