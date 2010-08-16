/** 
 * generates the a binary widget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.binary", {
	_init: function (){
		var that = this;

		if(this._getData('value'))
			var img = this._getData('imgOn');
		else
			var img = this._getData('imgOff');

		this.element.protowidget( $.extend({}, this.options, {
			createCopy: function(){
				return $('<div></div>').binary($.extend({}, that.options, {
					is_active: true,
					parentObj: that.element
				}));
			}
		}));
		
		this.img = $("<img></img>")
		.css({
			position: 	"absolute",
			height: 	(this._getData('height')) + "px",
			width:		(this._getData('width'))  + "px",
			top: 		this.element.protowidget('option', 'hoff') + "px",
			left: 		"0px"})
		.attr("src", img)
		.click(function (){
			if(!that.element.protowidget('option', 'isDragged'))
				that._getData('change')();
			else
				that.element.protowidget('option', 'isDragged', false);
		})
		.appendTo(this.element);
		
	},
	update_value: function(newValue){
		//updates UI
		if(newValue)
			this.img.attr("src", this._getData('imgOn'));
		else
			this.img.attr("src", this._getData('imgOff'));
		this._setData('value', newValue);
	}
});


$.extend($.ui.binary, {
    version: "1.0",     //version number
    defaults: {

top: 0,                 //top value of the widget
left: 0,                //left value of the widget
height: 160,            //height value of the widget
width: 160,	            //width value of the widget
imgOn: "/images/lightbulb1.png",
//img in widget when value = true

imgOff: "/images/lightbulb1off.png",
//img in widget when value = false

click: function(){},    //callback is called on click
stop: function(){},     //callback is called when drag stopped
prefix: "lamp",         //prefix for the id atribute
admin: null,            //true = admin mode - false = normal mode
admin_img: [     "ui-icon-help",
                 "ui-icon-wrench",	
                 "ui-icon-trash",	
                 "ui-icon-minus"],
//array filled with css classes to display
//the clickable icons of the admin widget

admin_onClick: [ function(){},	
                 function(){},		
                 function(){},
                 function(){}],
//callback is called on the click on
//the associated icon of the admin widget

text: "",              //text for the title bar
is_active: false,
//determines if a widget is in the favorites bar or not

favorites: null,
//reference to the JQuery object of the favorites bar

parentTag: null,
//reference to the JQuery object of the main area

active: 	   function(){},
//this callback is called when a widget is promoted to favorite

inactive: 	   function(){},
//this callback is called when a widget looses it's
//favorite status

admin_url: 		[ "",
           		  "" ],
//these strings are urls that are used for creating the help and
//the configuration dialog

in_toolbox:		function(){},
//this callback is called when a widget is moved to the
//admin sidebar

out_toolbox:	function(){},
//this callback is called when a widget is removed from the
//admin sidebar

value: true,		     //true -> on; false -> off

change:			function(){}
//this callback is called when the status of the lamp was
//changed by clicking

	}
});
})(jQuery);
