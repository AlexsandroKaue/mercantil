$('[role="form"]').validate({
	errorElement: 'span',
	errorPlacement: function (error, element) {
		error.addClass('invalid-feedback');
		element.closest('.form-group').append(error);
	},
	highlight: function (element, errorClass, validClass) {
		$(element).addClass('is-invalid');
	},
	unhighlight: function (element, errorClass, validClass) {
		$(element).removeClass('is-invalid');
	}
});

$.validator.addMethod('celular', function (value, element) {
    value = value.replace(/[_()-\s]/g, "");
    if (value == '0000000000') {
        return (this.optional(element) || false);
    } else if (value == '00000000000') {
        return (this.optional(element) || false);
    } 
    if (["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10"]
    .indexOf(value.substring(0, 2)) != -1) {
        return (this.optional(element) || false);
    }
    if (value.length < 11) {
        return (this.optional(element) || false);
    }
    if (["6", "7", "8", "9"].indexOf(value.substring(2, 3)) == -1) {
        return (this.optional(element) || false);
    }
    return (this.optional(element) || true);
}, 'Informe um número de telefone celular válido');

$.validator.addMethod('filesize', function (value, element, param) {
    return this.optional(element) || (element.files[0].size <= (param*1024*1024))
}, 'A imagem deve ser menor que {0}MB');


$.validator.addMethod("customDate", function(value, element) {
	if(value){
		return moment(value, 'DD/MM/YYYY', true).isValid();
	} 
	return true;
}, "Por favor forneça uma data válida");

$.validator.addMethod("customTime", function(value, element) {
	return moment(value, 'DD/MM/YYYY HH:mm', true).isValid();
}, "Por favor forneça uma data válida");

$.validator.addMethod("greaterThan", function(value, element, param) {
	value = Number(value.toString().replace(',','.'));
	param = Number(param.toString().replace(',','.'));
	if(value >= param){
		return true;
	}
	return false;
}, "Saldo insuficiente.");

$(function(){
	inicializar();
	var currencyformatter = new Intl.NumberFormat('pt-BR', {
	  style: "currency",
	  currency: "BRL",
	  minimumFractionDigits: 2,
	  currencyDisplay: "symbol",
	});

	var decimalformatter = new Intl.NumberFormat('pt-BR', {
	  minimumFractionDigits: 2
	});
	var seletor = $('#consultaPreco');
	var url = seletor.data('url-buscar-item');
	var placeholder = '&#xf51a Consulte produto aqui';
	$('#consultaPreco').select2({
		language: "pt-BR",
		ajax: {
		    url: url,
		    dataType: 'json',
		    delay: 250,
		    data: function (params) {
		      return {
		        q: params.term, // search term
		        page: params.page || 1 
		      };
		    },
		    processResults: function (data, params) {
		      // parse the results into the format expected by Select2
		      // since we are using custom formatting functions we do not need to
		      // alter the remote JSON data, except to indicate that infinite
		      // scrolling can be used
	    	  params.page = params.page || 1;
	  
		      return {
		        results: data.items,
		        pagination: {
		          more: (params.page * data.page_size) < data.total_count
		        }
		      };
		    },
    		cache: true
    	},
      allowClear: true,
	  placeholder: '<i class="fas fa-search"></i> Clique aqui para consultar preços',
	  escapeMarkup: function(m) {
		  return m; 
	  },
	  minimumInputLength: 1,
	  allowClear: true,
	  templateResult: formatRepo,
	  templateSelection: formatRepoSelection,
	  multiple: false,
		dropdownCssClass : 'bigdrop'
	});
	
	function formatRepo (repo) {
  	  if (repo.loading) {
  	    return repo.text;
  	  }
  	  
  	  var $container = $(
  	    "<div class='products-list product-list-in-card  py-2' style='min-height: 30px'> "+
  	      "<div class='product-img'>"+
  	      	"<img id='imagem' class='img-size-50' src='' />"+
	      "</div>"+
	      "<div class='product-info pl-1'>"+
	      	"<p class='product-description mb-0'></p>"+
	        "<h5 id='preco' class='float-right'></h5>"+
	        "<h5 class='product-title mb-0'></h5>"+
	      "</div>"+
  	    "</div>"
  	    	
  	    	/*"<li class='products-list product-list-in-card  py-2' role='li'>"+
  	      "<div class='product-img'>"+
  	      "</div>"+
  	      "<div class='product-info ml-4'>"+
  	      	"<p class='product-description mb-0'></p>"+
  	        "<h5 id='preco' class='float-right'></h5>"+
  	        "<h5 class='product-title mb-0'></h5>"+
  	      "</div>"+
  	    "</li>"*/
  	  );

	  $container.find("#imagem").attr('src', 'data:image/png;base64,'+repo.imagemBase64);
	  $container.find(".product-title").text(repo.descricao);
	  $container.find("#preco").text(currencyformatter.format(repo.valorDeVenda)+' '+repo.unitario);
	  $container.find(".product-description").text(repo.codigo);
	
	  return $container;
  	}

  	function formatRepoSelection (repo) {
  	  return repo.full_name || repo.text;
  	}
});



function inicializar() {
	
	$('[data-toggle="popover"]').popover();
	
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

	

	$('[rel="tooltip"]').tooltip({
		container: 'body',
		boundary: 'scrollParent',
		trigger : 'hover'
	});
	
	//Bootstrap Duallistbox
    $('.duallistbox').bootstrapDualListbox();
    
    /*$( "input[value='0']" ).text( "0" );*/
	    
	$('.js-currency').maskMoney({decimal:',', 
		thousands:'.', 
		allowZero:'false',
		formatOnBlur:false});
	
	$('.js-currency').maskMoney({decimal:',', 
		thousands:'.', 
		allowZero:'false',
		decimals: 3,
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
	    showDropdowns: true,
	    changeMonth: true,
	    changeYear: true,
	    minYear: 1901,
	    autoUpdateInput: false,
	    locale: {
	    	cancelLabel: 'Clear',
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
	    },
	    
	}).inputmask("99/99/9999");
	
	$('.js-date').on('apply.daterangepicker', function(ev, picker) {
		if (!picker.startDate.isValid()) {
			$(this).val('');
		} else {
			$(this).val(picker.startDate.format('DD/MM/YYYY'));
		}
	});
	
	//Date range picker
	$('.js-date-time').daterangepicker({
	    singleDatePicker: true,
	    /*showDropdowns: true,*/
	    timePicker: true,
	    timePicker24Hour: true,
	    showDropdowns: true,
	    changeMonth: true,
	    changeYear: true,
	    minYear: 1901,
	    autoUpdateInput: false,
	    locale: {
	    	cancelLabel: 'Clear',
	        format: "DD/MM/YYYY HH:mm",
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
	    },
	    
	}).inputmask("99/99/9999 99:99");;
	
	$('.js-date-time').on('apply.daterangepicker', function(ev, picker) {
		if (!picker.startDate.isValid()) {
			$(this).val('');
		} else {
			$(this).val(picker.startDate.format('DD/MM/YYYY HH:mm'));
		}
	});
	
	$('.js-tabela').DataTable({
      paging: true,
      pageLength: 10,
      lengthChange: false,
      searching: false,
      info: true,
      autoWidth: false,
      responsive: true,
      aaSorting: [],
      /*"lengthMenu": [ 5, 10, 15, 20 ],*/
      columnDefs:[{ "orderable": false, "targets": [-1] }],
      ordering: true,
      language: {
          lengthMenu: "Mostrar _MENU_ registro por página",
          info: "Mostrando _START_ a _END_ de _TOTAL_ registros",
          infoEmpty: "Nenhum registro encontrado",
          infoFiltered: "teste",
          search: "Pesquisa",
          zeroRecords:    "Nenhum registro encontrado",
          paginate: {
              first:      "Primeiro",
              last:       "Último",
              next:       "Próximo",
              previous:   "Anterior"
          }
      },
      dom: "<'row'<'col-sm-5'i><'col-sm-7'p>>" +
      "<'row'<'col-sm-12'tr>>" +
      "<'row'<'col-sm-5'i><'col-sm-7'p>>",
      destroy: true
    });
	
	$('.js-fade').fadeTo(5000, 1, function() {
		$(this).slideUp( "slow", function() {});
	});	
	
}

$(window).on('DOMContentLoaded',function(){
	var url = window.location.href;
    // for sidebar menu entirely but not cover treeview
	//console.log($('ul.nav-sidebar a'));
    $('ul.nav-sidebar a').filter(function() {
        return url.indexOf(this.href) <= -1;
    }).removeClass('active');

    // for sidebar menu entirely but not cover treeview
    $('ul.nav-sidebar a').filter(function() {
        return url.indexOf(this.href) > -1;
    }).addClass('active');
    
    // for treeview
    $('ul.nav-treeview a').filter(function() {
        return url.indexOf(this.href) > -1;
    }).parents(".has-treeview").addClass('menu-open');
    
    $('ul.nav-treeview a').filter(function() {
        return url.indexOf(this.href) > -1;
    }).parents(".has-treeview").children('a').addClass('active');
});


$(document).ajaxSend(function(e, xhr, options) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	xhr.setRequestHeader(header, token);
});

window.onload = function(){
	carretToEnd($('[autofocus="autofocus"]'));
	$('.select2').select2();
}

function carretToEnd(myElement){
	if($(myElement).is('input')){
		$(myElement).prop('selectionStart', $(myElement).val().length);
	}
}


	





