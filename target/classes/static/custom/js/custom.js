$('#modalConfirmacao').on('show.bs.modal', function(event){
	
	var button = $(event.relatedTarget); // Button that triggered the modal
	var codigo = button.data('codigo'); // Extract info from data-* attributes
	var descricao = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	/*var action = form.attr('action');*/
	if(!action.endsWith('/')){
		action += '/';
	}
	form.attr('action',action + codigo);
	modal.find('.modal-body p').html('Deseja realmente excluir o título <strong>'+descricao+'</strong>?');
	/*alert(codigo);*/
	
});


$(function(){
	$('[rel="tooltip"]').tooltip(
		{container: 'body'}
	);
	$('.js-currency').maskMoney({decimal:',', thousands:'.', allowZero:'true'})
	
	//Date range picker
	$('#inputDataVencimento').daterangepicker({
	    singleDatePicker: true,
	    /*showDropdowns: true,*/
	    minYear: 1901,
	    /*maxYear: parseInt(moment().format('YYYY'),10),*/
	    locale: {
	        format: "DD/MM/YYYY",
	        separator: " - ",
	        applyLabel: "Aplicar",
	        cancelLabel: "Cancelar",
	        fromLabel: "De",
	        toLabel: "Até",
	        customRangeLabel: "Custom",
	        daysOfWeek: [
	            "Do",
	            "Se",
	            "Te",
	            "Qu",
	            "Qu",
	            "Se",
	            "Sa"
	        ],
	        monthNames: [
	            "Janeiro",
	            "Fevereiro",
	            "Março",
	            "Abril",
	            "Maio",
	            "Junho",
	            "Julho",
	            "Agosto",
	            "Setembro",
	            "Outubro",
	            "Novembro",
	            "Dezembro"
	        ],
	        firstDay: 0
	    }
	});

})

