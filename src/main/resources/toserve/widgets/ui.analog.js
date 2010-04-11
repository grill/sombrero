(function($) {
	
$.widget("ui.analog", {
	_init: function (){
		var slide_rect = this._getData('slideRect');
		var inRange = false;
		var height = this._getData('height');
		var width = this._getData('width');
		var top = this._getData('top');
		var left = this._getData('left');
		var that = this;
		
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");
			
		$("<div></div>")
		.attr("style", "position:absolute;z-index:1;" + 
				"height:" 	+ height 	+ "px;" +
				"width:" 	+ width 	+ "px;" + 
				"top:" 		+ top 		+ "px;" +
				"left:" 	+ left 		+ "px;")
		.attr("class", "ui-state-default ui-corner-all " + this._getData('prefix') + "drag")
		.attr("id", this._getData('prefix') + "blank")
		.appendTo(this.element);
		
		$("<div></div>")
		.attr("style", "position:absolute;z-index:2;" + 
				"height:" 	+ height 	+ "px;" +
				"width:" 	+ width 	+ "px;" + 
				"top:" 		+ top 		+ "px;" +
				"left:" 	+ left 		+ "px;")
		.attr("class", this._getData('prefix') + "drag")
		.attr("id", this._getData('prefix') + "cut")
		.html('<img src="' + this._getData('backgroundImg') + '" />')
		.appendTo(this.element);
		
		$("<div></div>")
		.attr("style", "position:absolute;z-index:3;" +
				"background-image:url(" + this._getData('frontImg') + ");" + 
				"height:" 	+ height	+ "px;" +
				"width:" 	+ width 	+ "px;" + 
				"top:" 		+ top 		+ "px;" +
				"left:" 	+ left 		+ "px;")
		.attr('id', this._getData('prefix') + this._getData('imgId'))		
				
		.draggable({
			cancel: "#mid",
			drag: function(event, ui){
				that.drag(ui.position.top, ui.position.left);
			},
			stop: function(event, ui){
				that._getData('drag')();
				isDragged = true;
			}
		})
		
		.hover(function(){ that.mouseOn(); },
			function(){ that.mouseOff(); })
		
		.appendTo(this.element);

		var pos = $("#" + this._getData('prefix') + this._getData('imgId')).position();
		
		/*this.element.toolbox({
			top: pos.top + height - 5*20,
			left: pos.left + (width+4)
		});*/
		
		$("<div></div>")
		.attr("style", "position:absolute;z-index:4;" +
				"height:" 	+ slide_rect[2] 				+ "px;" +
				"width:" 	+ slide_rect[3] 				+ "px;" + 
				"top:" 		+ (pos.top + slide_rect[0]) 	+ "px;" +
				"left:" 	+ (pos.left + slide_rect[1])	+ "px;" + 
				"border:0px solid black;")
		.attr("id", this._getData('prefix') + "mid")
		.text(" ")
				
		.mousemove(function(e){
			var x = slide_rect[1] + (e.pageX - this.offsetLeft);
			var y = slide_rect[0] + (e.pageY - this.offsetTop);
			
			if(inRange){
				$("#" + that._getData('prefix') + "cut").css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
			}
		})
		
		.mousedown(function(e){
			var x = slide_rect[1] + (e.pageX - this.offsetLeft);
			var y = slide_rect[0] + (e.pageY - this.offsetTop);
		
			$("#" + that._getData('prefix') + "cut").css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
			inRange = true;
		})
		
		.mouseup(function(e){
			//var y = slide_rect[0] + (e.pageY - this.offsetTop);
			
			inRange = false;
			that._setData('temp', e.pageY - this.offsetTop);
			that._getData('change')();
		})
		
		.hover(function(){ that.mouseOn(); },
			function(){ that.mouseOff(); })
		.appendTo(this.element);
		
		this.element.disableSelection();
	},
	setMode: function(mode){
		if(mode == "admin"){
			this.adminMode();
		}else if(mode == "user"){
			this.element.admin("destroy");
		}
	},
	adminMode: function(){
		this.element.admin({
			top: this._getData('top'),
			left: this._getData('left'),
			width: this._getData('width'),
			height: this._getData('height'),
			parent: this,
			stop: "setPosition"
		});
	},
	mouseOn: function(){
		$("#" + this._getData('prefix') + "blank")
			.addClass("ui-state-hover");
	},
	mouseOff: function(){
		$("#" + this._getData('prefix') + "blank")
			.removeClass("ui-state-hover");
	},
	drag: function(top, left){
		var slideRect = this._getData('slideRect');
		
		$("." + this._getData('prefix') + "drag").css("top", top + "px");
		$("." + this._getData('prefix') + "drag").css("left", left + "px");
		$("#" + this._getData('prefix') + "mid")
			.css("top", top + slideRect[0] + "px");
		$("#" + this._getData('prefix') + "mid")
			.css("left", left + slideRect[1] + "px");
		this._setData('top', top);
		this._setData('left', left);
		//this._getData('drag')();
	},
	setPosition: function(top, left){
		$("#" + this._getData('prefix') + this._getData('imgId'))
			.css("top", top + "px");
		$("#" + this._getData('prefix') + this._getData('imgId'))
			.css("left", left + "px");
		this._setData('top', top);
		this._setData('left', left);
		//this._getData('drag')(top, left);
	}
});

$.extend($.ui.analog, {
	version: "1.0",
	defaults: {
	    top: 0,
	    left: 0,
		height: 300,
		width: 212,
	    backgroundImg: "/images/temperature_color.png",
	    frontImg: "/images/temperature_scala.png",
	    slideRect: [19, 45, 240, 106], 	//top left height width
	    change: function(){},
	    stop: function(){},
	    prefix: "",							//prefix for the id atribute
	    imgId: "frontImg",
	    temp: 0,
		admin_img: [     "ui-icon-wrench",	//array filled with css classes to display
			             "ui-icon-trash",	//the clickable icons of the admin widget
			             "ui-icon-plus",
			             "ui-icon-minus",
			             "ui-icon-close"],
		admin_onClick: [ function(){},		//callback is called on the click on
		                 function(){},		//the associated icon of the admin widget
		                 function(){},
		                 function(){},
		                 function(){}]
	}
});

})(jQuery);