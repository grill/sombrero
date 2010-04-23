(function($) {

$.widget("ui.unary", {
	_init: function (){
		var that = this;
	
		this.element.protowidget( $.extend({}, this.options, {
			createCopy: function(){
				return $('<div></div>').unary($.extend({}, that.options, {
					is_active: true,
					parentObj: that.element
				}));
			}
		}));
		
		$("<img></img>")
		.css({
			position: 	"absolute",
			height: 	(this._getData('height')) + "px",
			width:		(this._getData('width'))  + "px",
			top: 		this.element.protowidget('option', 'hoff') + "px",
			left: 		"0px"})
		.attr("id", this._getData('prefix') + this._getData('imgId'))
		.attr("src", this._getData('img'))
		
		.click(function (){
			if(!that.element.protowidget('option', 'isDragged')){
				that._getData('change')();
			}else
				that.element.protowidget('option', 'isDragged', false);

		})
		.appendTo(this.element);
		
	}
});

$.extend($.ui.unary, {
	version: "1.0",
	defaults: {
	    top: 0,
	    left: 0,
		height: 160,
		width: 160,
		img: "/images/Toggle.png",
		click: function(){},
	    stop: function(){},
	    prefix: "",
	    imgId: "img",
		icon_images: [   "ui-icon-wrench",
			             "ui-icon-trash",
			             "ui-icon-plus",
			             "ui-icon-minus",
			             "ui-icon-close"],
		admin_onClick: [ function(){},
		                 function(){},
		                 function(){},
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
		hoveroff:		false,
		in_toolbox:		function(){},
		out_toolbox:	function(){},
	    value: 			true,				//true -> on - false -> off
		change:			function(){}
	}
});
})(jQuery);
