(function($) {

$.widget("ui.unary", {
	_init: function (){
		var that = this;
		var isDragged = false;
	
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('suffix') + "_");
		
		$("<img></img>")
		.attr("style", "position:absolute;" +
				"height: " 	+ this._getData('height') 	+ "px;" +
				"width: " 	+ this._getData('width') 	+ "px;" +
				"top:" 		+ this._getData('top') 		+ "px;" +
				"left:" 	+ this._getData('left') 	+ "px;")
		.attr("class", "ui-state-default ui-corner-all")
		.attr("id", this._getData('prefix') + this._getData('imgId'))
		.attr("src", this._getData('img'))
		
		.click(function (){
			if(!isDragged)
				that._getData('click')();
			else
				isDragged = false;

		})
		
		.hover(function(){ that.mouseOn(); },
			function(){ that.mouseOff(); })
			
		.draggable({
			stop: function(event, ui){
				that._getData('drag')();
				isDragged = true;
				that._setData('top', ui.position.top);
				that._setData('left', ui.position.left);
			}
		})
		.appendTo(this.element);
	},
	setPosition: function(top, left){
		$("#" + this._getData('prefix') + this._getData('imgId'))
		.css("top", top + "px")
		.css("left", left + "px");
		this._setData('top', top);
		this._setData('left', left);
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
			drag: "setPosition"
		});
	},
	mouseOn: function(){
		$("#" + this._getData('prefix') + this._getData('imgId'))
			.addClass("ui-state-hover");
	},
	mouseOff: function(){
		$("#" + this._getData('prefix') + this._getData('imgId'))
			.removeClass("ui-state-hover");
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
		                 function(){}]
	}
});
})(jQuery);
