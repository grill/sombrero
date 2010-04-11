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
		var hoff = 33;
		var off = 2;
		
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");

		this.element.protowidget( $.extend({cancel: "#" + this._getData('prefix') + "mid"}, this.options, {
			createCopy: function(){
				return $('<div></div>').analog($.extend({cancel: "#" + that._getData('prefix') + "mid"}, that.options, {
					is_active: true,
					parentObj: that.element
				}));
			}
		}));
		
		var backimg = $("<div></div>")
		.css({
			position: 	"absolute",
			height:  	height + "px",
			width: 		width + "px", 
			top: 		hoff + "px",
			left: 		"0px"})
		.css("z-index", "2")
		.attr("id", this._getData('prefix') + "cut")
		.html('<img src="' + this._getData('backgroundImg') + '" />')
		.appendTo(this.element);
		
		var frontimg = $("<div></div>")
		.css({
			position: 	"absolute",
			height:  	height + "px",
			width: 		width + "px", 
			top: 		hoff + "px",
			left: 		"0px"})
		.css("z-index", "3")
		.css("background-image", "url(" + this._getData('frontImg') + ")")
		.attr('id', this._getData('prefix') + this._getData('imgId'))		
		.appendTo(this.element);
		
		if(this._getData('opacity') != ""){
			var optimg = $("<div></div>")
			.css({
				position: 	"absolute",
				height:  	height + "px",
				width: 		width + "px", 
				top: 		hoff + "px",
				left: 		"0px",
				opacity: 	"1"})
			.css("z-index", "4")
			.css("background-image", "url(" + this._getData('opacity') + ")")		
			.appendTo(this.element);
		}
		
		$("<div></div>")
		.css({
			position: 	"absolute",
			height:  	slide_rect[2] + "px",
			width: 		slide_rect[3] + "px", 
			top: 		(hoff + slide_rect[0])+ "px",
			left: 		(slide_rect[1])		+ "px",
			border:		"0px solid black"})
		.css("z-index", "4")
		.attr("id", this._getData('prefix') + "mid")
		.text(" ")
		.mousedown(function(e){
			//alert(slide_rect[0]);
			
			var x = /*slide_rect[1] +*/ (e.pageX- that.element.offset().left);//(e.pageX- this.offsetLeft);
			var y = /*slide_rect[0] +*/ (e.pageY- that.element.offset().top)-hoff;//(e.pageY- this.offsetTop);
		
			if(that._getData('clip_front'))
				if(that._getData('reverse'))
					backimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
				else
					backimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
			else
				if(that._getData('reverse'))
					frontimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
				else
					frontimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
			
			if(that._getData('opacity') != ""){
				optimg.css("opacity", ((y-slide_rect[0])/slide_rect[2]-1)*-1);
			}	
			inRange = true;
		})
		.appendTo(this.element);
		
		$('html')
		.mousemove(function(e){
			if(inRange){
				var x = /*slide_rect[1] +*/ (e.pageX-that.element.offset().left);//(e.pageX- this.offsetLeft);
				var y = /*slide_rect[0] +*/ (e.pageY-that.element.offset().top)-hoff;//(e.pageY- this.offsetTop);
				
				if(that._getData('clip_front'))
					if(that._getData('reverse'))
						backimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
					else
						backimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
				else
					if(that._getData('reverse'))
						frontimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
					else
						frontimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
				if(that._getData('opacity') != ""){
					optimg.css("opacity", ((y-slide_rect[0])/slide_rect[2]-1)*-1);
				}
			}
		})
		.mouseup(function(e){
			var y = (e.pageY- that.element.offset().top)-hoff;
			
			if(inRange){
				inRange = false;
				that._setData('temp', ((y-slide_rect[0])/slide_rect[2]-1)*-1);
				//alert(that._getData('temp'));
				that._getData('change')();
			}
		});
		
		this.element.titlebar({
			top: 5,
			left: 4,
			width: this._getData('width')-8,
			height: hoff,
			text:	this._getData('text')
    	});
    	
		this.element.disableSelection();
	}
});

$.extend($.ui.analog, {
	version: "1.0",
	defaults: {
	    top: 0,
	    left: 0,
		height: 160,
		width: 160,
	    backgroundImg: "/images/temperatur0bg.png",
	    frontImg: "/images/temperatur0fg.png",
	    slideRect: [9, 59, 122, 42], 	//top left height width
	    change: function(){},
	    stop: function(){},
	    prefix: "",							//prefix for the id atribute
	    imgId: "frontImg",
	    temp: 0,
		admin_img: [     "ui-icon-help",
		                 "ui-icon-wrench",	//array filled with css classes to display
			             "ui-icon-trash",	//the clickable icons of the admin widget
			             "ui-icon-minus"],
		admin_onClick: [ function(){},		//callback is called on the click on
		                 function(){},		//the associated icon of the admin widget
		                 function(){},
		                 function(){}],
		text: "",
		clip_front: false,
		reverse: false,		//direction from up to down -> true
		opacity: "",
		is_active: false,
		favorites: null,
		parentTag: null,
		active: 	   function(){},
		inactive: 	   function(){},
		admin_url: 		[ "",
		           		  "" ],
		in_toolbox:		function(){},
		out_toolbox:	function(){}
	}
});

})(jQuery);