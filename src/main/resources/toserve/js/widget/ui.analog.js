/** 
 * generates the a analog widget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.analog", {
  options: {
    version: "2.0",
    backgroundImg: "/classpath/images/temperatur0bg.png",
    frontImg: "/classpath/images/temperatur0fg.png",
    slideRect: [9, 59, 122, 42],  //top left height width
    clipFront: false,
    reverse: false,   //direction from up to down -> true
    opacity: "",
    value:      0,            //true -> on - false -> off
    change:     function(){},
    //-----------------------------------------------------
    //The following Options are inherited from simplewidget
    //-----------------------------------------------------
    height: 160,
    width: 160,
    top:                0,            
    left:               0, 
    heightOffset:       33,
    offset:             2,            
    stop:               "", //code is evaluated after dragging has stopped
    isHover:            true,              
    //This array determines the amount and the ability of
    //the items in the widget toolbar
    //1.: icon class; 2.: code that's evaluated after click on the respective icon
    //3.: URL that will be opened in a dialog after click
    toolbox:         [ ["ui-icon-help",   "", ""],
                   ["ui-icon-wrench", "", ""],
                   ["ui-icon-trash",  "", ""],  
                   ["ui-icon-minus",  "", ""] ],
    enterToolbox:         "",
    leaveToolbox:        "",
    text:               "",
    isDragged:          false,
    isAdminMode:        false,  //true -> admin mode - false -> normal mode
    adminSidebarTag:    "",
    isFavorite:         false,
    favoriteTag:        "",
    containment:        "",
    parentWidget:       "",   //id of parent widget
    active:             "",
    inactive:           "",
    cancel:             "",
    copy:               ""
  },
  _create: function (){
    var slideRect = this.options.slideRect;
		var inRange = false;
		var height = this.options.height;
		var width = this.options.width;
		var top = this.options.top;
		var left = this.options.left;
		var that = this;
		var hoff = 33;
		var off = 2;

		this.element.simplewidget( $.extend({cancel: "#mid"}, this.options, {
			createCopy: function(){
				return $('<div></div>').analog($.extend({cancel: "#mid"}, that.options, {
          isFavorite: true,
          parentWidget: that.element
				}));
			}
		}));
		
		this.backimg = $("<div></div>")
		.css({
			position: 	"absolute",
			top: 		hoff + "px",
			left: 		"0px"})
		.css("z-index", "2")
		.attr("id", "cut")
		.html('<img src="' + this.options.backgroundImg + '" />')
		.appendTo(this.element);
		
		this.frontimg = $("<div></div>")
		.css({
			position: 	"absolute",
			height:  	height + "px",
			width: 		width + "px", 
			top: 		hoff + "px",
			left: 		"0px"})
		.css("z-index", "3")
		.css("background-image", "url(" + this.options.frontImg + ")")
		.attr('id', "frontImg")		
		.appendTo(this.element);
		
		if(this.options.opacity != ""){
			this.optimg = $("<div></div>")
			.css({
				position: 	"absolute",
				height:  	height + "px",
				width: 		width + "px", 
				top: 		hoff + "px",
				left: 		"0px",
				opacity: 	"1"})
			.css("z-index", "4")
			.css("background-image", "url(" + this.options.opacity + ")")		
			.appendTo(this.element);
		}
		
		$("<div></div>")
		.css({
			position: 	"absolute",
			height:  	slideRect[2] + "px",
			width: 		slideRect[3] + "px", 
			top: 		(hoff + slideRect[0])+ "px",
			left: 		(slideRect[1])		+ "px",
			border:		"0px solid black"})
		.css("z-index", "4")
		.attr("id", "mid")
		.text(" ")
		.mousedown(function(e){
			var y = /*slideRect[0] +*/ (e.pageY- that.element.offset().top)-hoff;//(e.pageY- this.offsetTop);
		
			that.update_value(y);
			inRange = true;
		})
		.appendTo(this.element);
		
		$('html')
		.mousemove(function(e){
			if(inRange){
				var y = /*slideRect[0] +*/ (e.pageY-that.element.offset().top)-hoff;//(e.pageY- this.offsetTop);
				
				that.update_value(y);
			}
		})
		.mouseup(function(e){
			var y = (e.pageY- that.element.offset().top)-hoff;
		
			if(inRange){
				inRange = false;
				if(y <= 0)
					that._setOption('value', 1);
				else
					that._setOption('value', ((y-slideRect[0])/slideRect[2]-1)*-1);
				eval(that._getOption('change'));
			}
		});
		
		this.update_value(this.options.value);
		/*
		this.element.titlebar({
			top: 5,
			left: 4,
			width: this.options.width-8,
			height: hoff,
			text:	this.options.text
    	});
    	*/
		this.element.disableSelection();
	},
	update_value: function(y){
		//updates the UI
		var slideRect = this.options.slideRect;
		var height = this.options.height;
		var width = this.options.width;
		
		if(this.options.clipFront)
			if(this.options.reverse)
				this.backimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
			else
				this.backimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
		else
			if(this.options.reverse)
				this.frontimg.css("clip", "rect(0px, " + width + "px, " + y + "px, 0px)");
			else
				this.frontimg.css("clip", "rect(" + y + "px, " + width + "px, " + height + "px, 0px)");
		if(this.options.opacity != ""){
			this.optimg.css("opacity", ((y-slideRect[0])/slideRect[2]-1)*-1);
		}
		this._setOption('value', y);
	}
}); })(jQuery);