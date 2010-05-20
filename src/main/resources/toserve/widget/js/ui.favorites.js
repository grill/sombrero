/*-------------------------------------------------------------------- 
Scripts for creating and manipulating custom menus based on standard <ul> markup
Version: 3.0, 03.31.2009

By: Maggie Costello Wachs (maggie@filamentgroup.com) and Scott Jehl (scott@filamentgroup.com)
	http://www.filamentgroup.com
	* reference articles: http://www.filamentgroup.com/lab/jquery_ipod_style_drilldown_menu/
		
Copyright (c) 2009 Filament Group
Dual licensed under the MIT (filamentgroup.com/examples/mit-license.txt) and GPL (filamentgroup.com/examples/gpl-license.txt) licenses.
--------------------------------------------------------------------*/

/* And rewritten by Gabriel Grill for sombrero */


(function($) {

$.widget("ui.favorites", {
	_init: function (){
		this.toff = 10;
		this.loff = 20;
		this.wlen = 0;
		var that = this;
		this.width = this._getData('width') + 2*this.loff;
		this.height = this._getData('height') + 2*this.toff+2;
		var woff = 81;
		var hoff = 80;
		var index = 0;
		var max;
	
		if(this._getData('vertical')){
			woff = 0;
		}else{
			hoff = 0;
		}
		
		var kids = this.element.children();
		/*kids.each(function(){
			alert($(this).binary('option', 'value'));
			alert($(this).protowidget('option', 'prefix'));
		});*/
		/*if(kids.length > 0){
			kids.each(function(){
				$(this).detach();
			});
		}*/
		
		if(!this._getData('vertical')){
			this.createButton("/images/arrowleft.png")
			.css({
				height: 	this.height-2 + "px",
				top: 		"0px",
				left: 		"0px"})
			.click(function(){
				if(index > 0){
					index--;
					that.favbar.animate({left: '+=' + (that._getData('width')+that.loff)}, 500, function(){});
				}
			});
		}else{			
			this.createButton("/images/arrowup.png")
			.css({
				width: 	this.width-2 + "px",
				height: hoff/2 + "px",
				top: 		"0px",
				left: 		"0px"})
			.click(function(){
				if(index > 0){
					index--;
					that.favbar.animate({top: '+=' + (that._getData('height')+that.toff)}, 500, function(){});
				}
			});
		}
		
		var container = $("<div></div>")
		.css({
			position: 	"absolute",
			top: 		hoff/2 + "px",
			left: 		woff/2 + "px",
			overflow:	"hidden",
			height: 	this._getData('height') + "px"
			//border: 	"1px solid black"
			})
			.addClass("ui-corner-all")
			.addClass("ui-state-highlight")
		.appendTo(this.element);
		
		this.favbar = $("<div></div>")
		.css({
			position: 	"absolute",
			height: 	this._getData('height') + "px",
			top: 		"0px",
			left: 		"0px"})
		.appendTo(container);
		//.append(kids);
		
		if(kids.length > 0)
			kids.each(function(){
				//$(this)
				that.deactivate_and_append($(this));
				/*that.append($(this));
				//alert($(this).binary('option', 'value'));
				//alert($(this).protowidget('option', 'prefix'));
				$(this).draggable("destroy");
				if(!that._getData('admin_mode'))
					$(this).toolbox('destroy');*/
			});
		
		if(this._getData('vertical')){
			this.height = ((this._getData('amount_widgets')*(this._getData('height')+this.toff))+this.toff);
		}else{
			this.width = ((this._getData('amount_widgets')*(this._getData('width')+this.loff))+this.loff);
		}
		container.css({
			width: this.width-2 + "px",
			height: this.height-2 + "px"
		});		//-2 because of the border you need this offset

		this.element
		.css({
			position: 	"absolute",
			height: 	this.height+hoff 			+ "px",	//offset von 1
			width:		this.width+woff+1			+ "px",		//offset von 2
			top: 		this._getData('top') 	+ "px",
			left: 		this._getData('left') 	+ "px"
			//border: 	"1px solid black"
			});
		
		if(!this._getData('vertical')){
			var sbutton = this.createButton("/images/arrowright.png")
			.css({
				height: 	this.height-2 + "px",
				top: 		"0px",
				left: 		this.width+39 	+ "px"})
			.click(function(){
				if(index < that.wlen - that.options.amount_widgets){
					index++;
					that.favbar.animate({left: '-=' + (that._getData('width')+that.loff)}, 500, function(){});
				}
			});
			max = container.offset().left + that.width + 100;
			var adopt = function() {
				//alert("width: " + $(window).width() + " offs: " + container.offset().left + " mywidth: " + that.width);
				if($(window).width() < max/* container.offset().left + that.width+100*/){
					//alert($(window).width());
					that.width = ($(window).width()-container.offset().left)-100;//((that._getData('amount_widgets')*(that._getData('width')+that.loff))+that.loff)-5;
					that._setData('amount_widgets', (that.width / (162+2*that.loff)));
					container.css({width: that.width});
					sbutton.css({left: that.width+39 + "px"});
				}else{
					that.width = max - 100 - container.offset().left;
					that._setData('amount_widgets', (that.width / (162+2*that.loff)));
					container.css({width: that.width});
					sbutton.css({left: that.width+39 + "px"});
				}
			};
			adopt();
			$(window).wresize(adopt);
		}else{
			this.createButton("/images/arrowdown.png")
			.css({
				width: 	this.width-2 + "px",
				top: 	(this.height + hoff/2-1)+ "px",
				height: hoff/2-1,
				left:	"0px"})
			.click(function(){
				if(index < that.wlen - that.options.amount_widgets){
					index++;
					that.favbar.animate({top: '-=' + (that._getData('height')+that.toff)}, 500, function(){});
				}
			});
		}
	},
	deactivate_and_append: function(e){
		this.append(e);
		e.draggable("destroy");
		if(!this._getData('admin_mode'))
			e.toolbox('destroy');
	},
	append: function(e) {		
		this.favbar.append(e);
		this.add(e);
	},
	add: function(e){
		var top = this.toff;
		var left = this.loff;
		
		if(this.wlen == 0){
			if(this._getData('vertical'))
				top -= 2;
			else
				left -= 2;
		} else {
			if(this._getData('vertical'))
				top += e.prev().position().top + e.prev().height();
			else
				left += e.prev().position().left + e.prev().width();
		}
		e.css({
			top: top,
			left: left
		});
		this.wlen += 1;
	},
	remove: function(e) {
		this.move(e);
		e.remove ();
	},
	move: function(e) {
		var vertical = this._getData('vertical');
		if(vertical)
			var dec = e.height()+this.toff;
		else
			var dec = e.width()+this.loff;

		e.nextAll().each(function(){
			if(vertical)
				$(this).animate({
					top: $(this).position().top - dec
				}, 500, function(){});
			else
				$(this).animate({
					left: $(this).position().left - dec
				}, 500, function(){});
		});
		this.wlen -= 1;
	},
	createButton: function(img){
		if(this._getData('vertical'))
			var margin = {'margin-left':  ((this.width-1)/2)-30 + "px"};
		else
			var margin = {'margin-top':  ((this.height-1)/2)-30 + "px"};
		
		var button = $("<div></div>")
		.append($('<img></img>')
			.attr("src", img)
			.css( margin ))
		.addClass("ui-corner-all ui-state-default")
		.hover(function(){ $(this).addClass("ui-state-hover"); },
				function(){ $(this).removeClass("ui-state-hover"); })
		.mousedown(function(){
			$(this).addClass("ui-state-active"); 
		})
		.css({ position: 	"absolute"})
		.appendTo(this.element);
		$('html')
		.mouseup(function(){
			button.removeClass("ui-state-active");
		});
		return button;
	},
	destroy: function() {
		//$.ui.favorites.prototype.destroy.call(this);
	}
});


$.extend($.ui.favorites, {
	version: "1.0",
	defaults: {
		amount_widgets:	4,
		width:			162,		//2 wegen off
		height:			235,	//35 wegen off und hoff aus binary
	    top:			100,
	    left:			20,
		icon_images:    [ "ui-icon-wrench",
			              "ui-icon-trash",
			              "ui-icon-plus",
			              "ui-icon-minus",
			              "ui-icon-close"],
		admin_onClick:  [ function(){},
		                  function(){},
		                  function(){},
		                  function(){},
		                  function(){}],
		widgets: 		[],
		vertical: 		false,
		admin_mode:		false
	}
});
})(jQuery);
