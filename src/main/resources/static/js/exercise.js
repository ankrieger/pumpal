$(document).ready(function () {
    let max_fields = 30;
    let index = 1;


    $('.add-exercise').click(function () {

        if (index < max_fields) {
            let newRow = jQuery('<div class="exercise row mb-5">' +
                ' <div class="mb-3">' +
                ' Exercise description*:' +
                ' <input name="exercises[' + index + '].description" type="text" class="form-control form-control"/>' +
                ' </div>' +
                ' <div class="mb-3">' +
                '  Weight(in kg):' +
                ' <input name="exercises[' + index + '].weight" type="number" step="1" min="0"' +
                ' class="form-control form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Reps:' +
                ' <input name="exercises[' + index + '].sets" type="number" step="1" min="0"' +
                ' class="form-control form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                '  Sets:' +
                ' <input name="exercises[' + index + '].reps" type="number" step="1" min="0"' +
                ' class="form-control form-control"/>' +
                '</div>' +
                ' <div class="mb-3">' +
                ' Additional note:' +
                ' <input name="exercises[' + index + '].note" type="text" class="form-control form-control"/>' +
                ' </div>'+
                '</div>');

            index++;

            $('.exercises').append(newRow)
        }

    });
});