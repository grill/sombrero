(function($) {

$.widget("ui.binary", {
	_init: function (){
		var that = this;
		var isDragged = false;
	
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");
		
		if(this._getData('value'))
			var img = this._getData('imgOn');
		else
			var img = this._getData('imgOff');
		
		$("<img></img>")
		.attr("style", "position:absolute;" +
				"height: " 	+ this._getData('height') 	+ "px;" +
				"width: " 	+ this._getData('width') 	+ "px;" +
				"top:" 		+ this._getData('top') 		+ "px;" +
				"left:" 	+ this._getData('left') 	+ "px;")
		.attr("class", "ui-state-default ui-corner-all")
		.attr("id", this._getData('prefix') + this._getData('imgId'))
		.attr("src", img)
		
		.click(function (){
			if(!isDragged){
				that._getData('click')();
			}else{
				isDragged = false;
			}
		})
		
		.hover(function(){ that.mouseOn(); },
			function(){ that.mouseOff(); })
			
		.draggable({
			stop: function(event, ui){
				that._getData('drag')();
				that._setData('top', ui.position.top);
				that._setData('left', ui.position.left);
				isDragged = true;
			}
		})
		
		.appendTo(this.element);
	},
	setPosition: function(top, left){
		$("#" + this._getData('suffix') + this._getData('imgId'))
		.css("top", top + "px")
		.css("left", left + "px");
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
	},
	updateStatus: function(){
		var value = this._getData('value');
		
		if(value){
			$("#" + this._getData('prefix') + this._getData('imgId'))
			.attr("src", this._getData('imgOn'));
		}else{
			$("#" + this._getData('prefix') + this._getData('imgId'))
			.attr("src", this._getData('imgOff'));
		}
	}
});

$.extend($.ui.binary, {
	version: "1.0",							//version number
	defaults: {
	    top: 0,								//top value of the widget
	    left: 0,							//left value of the widget
		height: 200,						//height value of the widget
		width: 160,							//width value of the widget
	    value: true,						//true -> on - false -> off
		imgOn: "/images/lampLight.png",		//img in widget when value = true
		imgOff: "/images/lampNoLight.png",  //img in widget when value = false
		click: function(){},				//callback is called on click
	    stop: function(){},					//callback is called when drag stopped
	    prefix: "",							//prefix for the id atribute
	    imgId: "img",
	    admin_init: false,					//true -> admin mode - false -> normal mode
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
