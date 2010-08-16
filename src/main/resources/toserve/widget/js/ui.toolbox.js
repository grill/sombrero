/** 
 * generates the toolbox for a widget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.toolbox", {
	_init: function (){
	//alert("hi");
		var names = this._getData('admin_img');
		var callbacks = this._getData('admin_onClick');
		var that = this;
		var height = (21*names.length)+1;

		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");

		this.container = $("<div></div>")
		.css({
			position: 	'absolute',
			width:	  	'22px',
			height:		height,
			top:		this._getData('pheight')-height-3 + 'px', //-1 fuer vorher
			left:		this._getData('pwidth')-24  + 'px',//+ 5 fuer drausen
			"z-index":	1000
		})
		.attr("class", "ui-widget-header ui-corner-all")
		.appendTo(this.element);

		$.each(names, function(idx, value){
			var button = $('<button style="margin-top:1px;margin-left:1px;width:20px;height:20px" ' +
					'class ="' + value + ' ui-icon ui-button ui-state-default ui-corner-all"/>')
			.click(function(){
				if(idx == 3)
					callbacks[idx](button);
				else
					callbacks[idx]();
			})
			.hover(
				function(){ 
					$(this).addClass("ui-state-hover"); 
				},
				function(){ 
					$(this).removeClass("ui-state-hover"); 
				}
			)
			.mousedown(function(){
				$(this).addClass("ui-state-active"); 
			})
			/*.mouseup(function(){
				$(this).removeClass("ui-state-active");
			})*/;
			$('html')
			.mouseup(function(){
				button.removeClass("ui-state-active");
			});
			if(idx < 2){
				$("<a></a>")
				.attr("href", that._getData('admin_url')[idx])
		    	.append(button)
		    	.appendTo(that.container)
		    	.fancybox({
		    		'width'				: '75%',
		    		'height'			: '75%',
		            'autoScale'     	: false,
		            'transitionIn'		: 'none',
		    		'transitionOut'		: 'none',
		    		'type'				: 'iframe',
		    		'title'				: that._getData('title')[idx]
		    	});//wrap
			}else {
				button.appendTo(that.container);
			}
		});
	},
	destroy: function() {
		var it = this.container;
		
		it.hide("drop", {}, 0, function(){
			it.remove();
		});
		$.widget.prototype.destroy.apply(this, arguments);
	},
	reactivate: function(){
		alert("hi");
		var it = this.container;
		it.show("drop", {}, 500, function(){
			alert("hi");
			it.appendTo(this.element);
		});
	}
});

$.extend($.ui.toolbox, {
	version: "1.0",
	defaults: {
	    pheight: 0,	
	    pwidth: 0,
	    prefix: "",
		admin_img: 		[ "ui-icon-help",
		                  "ui-icon-wrench",	//array filled with css classes to display
			              "ui-icon-trash",	//the clickable icons of the admin widget
			              "ui-icon-minus"],
		admin_onClick: 	[ function(){},		//callback is called on the click on
		                  function(){},		//the associated icon of the admin widget
		                  function(){},
		                  function(){}],
		admin_url: 		[ "",				//these strings should contain URL's pointing
		           		  "" ],				//to the respective dialog content
		title:			["Help",			//titles for the dialogs
		      			 "Configure Widget" ]
	}
});

})(jQuery);
