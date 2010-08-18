/** 
 * generates a favorites widget or Admin's toolbox in the Sidebar
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.widgetcontainer", {
  options: {
    version: "2.0",
    amountWidgets: 4,      //amount of widgets displayed
    width:      162,    //width of widgets which will be inserted
    height:     235,    //height of widgets which will be inserted
    top:      100,
    left:     20,
    vertical:     false,    //if true favorites widget will be vertical aligned
                  //if false favorites widget will be horizontal aligned
    isAdminMode:   false   //if true the favorites widget becomes a toolbox
  },
	_create: function (){
		this.toff = 10;
		this.loff = 20;
		this.wlen = 0;
		var that = this;
		this.width = this.options.width + 2*this.loff;
		this.height = this.options.height + 2*this.toff+2;
		var woff = 81;
		var hoff = 80;
		var index = 0;
		var max;
	
		if(this.options.vertical){
			woff = 0;
		}else{
			hoff = 0;
		}
		
		var kids = this.element.children();
		
		if(!this.options.vertical){
			this.createButton("/classpath/images/arrowleft.png")
			.css({
				height: 	this.height-2 + "px",
				top: 		"0px",
				left: 		"0px"})
			.click(function(){
				if(index > 0){
					index--;
					that.favbar.animate({left: '+=' + (that._getOption('width')+that.loff)}, 500, function(){});
				}
			});
		}else{			
			this.createButton("/classpath/images/arrowup.png")
			.css({
				width: 	this.width-2 + "px",
				height: hoff/2 + "px",
				top: 		"0px",
				left: 		"0px"})
			.click(function(){
				if(index > 0){
					index--;
					that.favbar.animate({top: '+=' + (that._getOption('height')+that.toff)}, 500, function(){});
				}
			});
		}
		
		var container = $("<div></div>")
		.css({
			position: 	"absolute",
			top: 		hoff/2 + "px",
			left: 		woff/2 + "px",
			overflow:	"hidden",
			height: 	this.options.height + "px"
			//border: 	"1px solid black"
			})
			.addClass("ui-corner-all")
			.addClass("ui-state-highlight")
		.appendTo(this.element);
		
		this.favbar = $("<div></div>")
		.css({
			position: 	"absolute",
			height: 	this.options.height + "px",
			top: 		"0px",
			left: 		"0px"})
		.appendTo(container);
		//.append(kids);
		
		if(kids.length > 0)
			kids.each(function(){
				that.deactivate_and_append($(this));
			});
		
		if(this.options.vertical){
			this.height = ((this.options.amountWidgets*(this.options.height+this.toff))+this.toff);
		}else{
			this.width = ((this.options.amountWidgets*(this.options.width+this.loff))+this.loff);
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
			top: 		this.options.top 	+ "px",
			left: 		this.options.left 	+ "px"
			//border: 	"1px solid black"
			});
		
		if(!this.options.vertical){
			var sbutton = this.createButton("/classpath/images/arrowright.png")
			.css({
				height: 	this.height-2 + "px",
				top: 		"0px",
				left: 		this.width+39 	+ "px"})
			.click(function(){
				if(index < that.wlen - that.options.amountWidgets){
					index++;
					that.favbar.animate({left: '-=' + (that._getOption('width')+that.loff)}, 500, function(){});
				}
			});
			max = container.offset().left + that.width + 100;
			var adopt = function() {
				//alert("width: " + $(window).width() + " offs: " + container.offset().left + " mywidth: " + that.width);
				if($(window).width() < max/* container.offset().left + that.width+100*/){
					//alert($(window).width());
					that.width = ($(window).width()-container.offset().left)-100;//((that._getOption('amountWidgets')*(that._getOption('width')+that.loff))+that.loff)-5;
					that._setOption('amountWidgets', (that.width / (162+2*that.loff)));
					container.css({width: that.width});
					sbutton.css({left: that.width+39 + "px"});
				}else{
					that.width = max - 100 - container.offset().left;
					that._setOption('amountWidgets', (that.width / (162+2*that.loff)));
					container.css({width: that.width});
					sbutton.css({left: that.width+39 + "px"});
				}
			};
			adopt();
			$(window).wresize(adopt);
		}else{
			this.createButton("/classpath/images/arrowdown.png")
			.css({
				width: 	this.width-2 + "px",
				top: 	(this.height + hoff/2-1)+ "px",
				height: hoff/2-1,
				left:	"0px"})
			.click(function(){
				if(index < that.wlen - that.options.amountWidgets){
					index++;
					that.favbar.animate({top: '-=' + (that._getOption('height')+that.toff)}, 500, function(){});
				}
			});
		}
	},
	deactivate_and_append: function(e){
		this.append(e);
		e.draggable("destroy");
		if(!this.options.isAdminMode)
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
			if(this.options.vertical)
				top -= 2;
			else
				left -= 2;
		} else {
			if(this.options.vertical)
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
		var vertical = this.options.vertical;
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
		if(this.options.vertical)
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
}); })(jQuery);
