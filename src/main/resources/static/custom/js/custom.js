$('#modalConfirmacao').on('show.bs.modal', function(event){
	
	var button = $(event.relatedTarget); // Button that triggered the modal
	var id = button.data('id'); // Extract info from data-* attributes
	var descricao = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	/*var action = form.attr('action');*/
	if(!action.endsWith('/')){
		action += '/';
	}
	form.attr('action',action + id);
	modal.find('.modal-body p').html('Deseja realmente excluir o registro <strong>'+descricao+'</strong>?');
	/*alert(id);*/
	
});


$(function(){
	$('[rel="tooltip"]').tooltip({
		container: 'body',
		boundary: 'scrollParent',
		trigger : 'hover'
	});
	
	//Bootstrap Duallistbox
    $('.duallistbox').bootstrapDualListbox();
	
	$('.js-currency').maskMoney({decimal:',', 
		thousands:'.', 
		allowZero:'true',
		formatOnBlur:false});
	
	$('[data-mask]').inputmask();
	
	$('.js-atualizar-status').on('click', function(event) {
		event.preventDefault();
		
		var botaoReceber = $(event.currentTarget);
		var urlReceber = botaoReceber.attr('href');
		
		var response = $.ajax({
			url: urlReceber,
			type: 'PUT'
		})
		
		response.done(function(e){
			var idTitulo = botaoReceber.data('id');
			$('[data-role="'+ idTitulo + '"]').html('<span class="badge bg-success">' + e + '</span>');
			botaoReceber.hide();
		});
		
		response.fail(function(e){
			console.log(e);
			alert('Erro recebendo cobrança');
		});
	});
	
	//Date range picker
	$('.js-date').daterangepicker({
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
      "pageLength": 5,
      /*"lengthChange": true,*/
      "searching": false,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "lengthChange": false,
      "aaSorting": [],
      /*"lengthMenu": [ 5, 10, 15, 20 ],*/
      /*"columnDefs": [
    	    { "orderable": false, "targets": [0,5] }
    	  ],*/
      "ordering": true,
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
	
	
	$('.js-fade').fadeTo(5000, 1, function() {
		$(this).slideUp( "slow", function() {});
	})

	
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


