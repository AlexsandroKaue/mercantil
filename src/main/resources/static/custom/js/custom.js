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
	$('[rel="tooltip"]').tooltip({
		container: 'body'
	});
	
	$('.js-currency').maskMoney({decimal:',', thousands:'.', allowZero:'true'});
	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		})
		
		response.done(function(e){
			var codigoTitulo = botaoReceber.data('codigo');
			$('[data-role="'+ codigoTitulo + '"]').html('<span class="badge bg-success">' + e + '</span>');
			botaoReceber.hide();
		});
		
		response.fail(function(e){
			console.log(e);
			alert('Erro recebendo cobrança');
		});
	});
	
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
	
	$('.js-tabela').DataTable({
      "paging": true,
      "pageLength": 10,
      "lengthChange": true,
      "searching": false,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "lengthChange": false,
      /*"lengthMenu": [ 5, 10, 15, 20 ],*/
      /*"columnDefs": [
    	    { "orderable": false, "targets": [0,5] }
    	  ],*/
      "ordering": false,
      "language": {
          "lengthMenu": "Mostrar _MENU_ registro por página",
          "zeroRecords": "Nothing found - sorry",
          "info": "Total de registros: _MAX_",
          "infoEmpty": "Nenhum registro encontrado",
          "infoFiltered": "",
          "search": "Pesquisa",
          "zeroRecords":    "Nenhum registro encontrado",
          "paginate": {
              "first":      "Primeiro",
              "last":       "Último",
              "next":       "Próximo",
              "previous":   "Anterior"
          }
      }
    });

	
    /*const Toast = Swal.mixin({
    	
      var saved = [[${!#strings.isEmpty(mensagem)}]];
      
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000
    });
    if(saved){
  	  toastr.success([[${mensagem}]]);  
    }*/
    
});


