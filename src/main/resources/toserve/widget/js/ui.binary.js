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
<<<<<<< HEAD:src/main/resources/toserve/widget/js/ui.binary.js
				that._getData('change')();
=======
				that._getData('click')();
>>>>>>> origin/master:src/main/resources/toserve/widget/js/ui.binary.js
			else
				that.element.protowidget('option', 'isDragged', false);
		})
		.appendTo(this.element);
		
	},
<<<<<<< HEAD:src/main/resources/toserve/widget/js/ui.binary.js
	update_value: function(newValue){
		if(newValue)
			this.img.attr("src", this._getData('imgOn'));
		else
			this.img.attr("src", this._getData('imgOff'));
		this._setData('value', newValue);
=======
	updateStatus: function(){
		if(this._getData('value'))
			this.img.attr("src", this._getData('imgOn'));
		else
			this.img.attr("src", this._getData('imgOff'));
>>>>>>> origin/master:src/main/resources/toserve/widget/js/ui.binary.js
	}
});

$.extend($.ui.binary, {
	version: "1.0",							//version number
	defaults: {
	    top: 0,								//top value of the widget
	    left: 0,							//left value of the widget
		height: 160,						//height value of the widget
		width: 160,							//width value of the widget
<<<<<<< HEAD:src/main/resources/toserve/widget/js/ui.binary.js
=======
	    value: true,						//true -> on - false -> off
>>>>>>> origin/master:src/main/resources/toserve/widget/js/ui.binary.js
		imgOn: "/images/lightbulb1.png",		//img in widget when value = true
		imgOff: "/images/lightbulb1off.png",  //img in widget when value = false
		click: function(){},				//callback is called on click
	    stop: function(){},					//callback is called when drag stopped
	    prefix: "lamp",							//prefix for the id atribute
	    imgId: "img",
	    admin: null,					//true -> admin mode - false -> normal mode
		admin_img: [     "ui-icon-help",
		                 "ui-icon-wrench",	//array filled with css classes to display
			             "ui-icon-trash",	//the clickable icons of the admin widget
			             "ui-icon-minus"],
		admin_onClick: [ function(){},		//callback is called on the click on
		                 function(){},		//the associated icon of the admin widget
		                 function(){},
		                 function(){}],
		text: "",
		is_active: false,
		favorites: null,
		parentTag: null,
		active: 	   function(){},
		inactive: 	   function(){},
		admin_url: 		[ "",
		           		  "" ],
		in_toolbox:		function(){},
<<<<<<< HEAD:src/main/resources/toserve/widget/js/ui.binary.js
		out_toolbox:	function(){},
	    value: 			true,				//true -> on - false -> off
		change:			function(){}
=======
		out_toolbox:	function(){}
>>>>>>> origin/master:src/main/resources/toserve/widget/js/ui.binary.js
	}
});
})(jQuery);
