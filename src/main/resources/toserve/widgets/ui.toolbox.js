(function($) {

$.widget("ui.toolbox", {
	_init: function (){
		var names = this._getData('admin_img');
		var callbacks = this._getData('admin_onClick');
		var that = this;
	
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");
		
		$("<div></div>")
		.attr("style", "position:absolute;width: 21px;")
		.attr("class", "ui-widget-header ui-corner-all")
		.attr("id", this._getData('prefix') + this._getData('imgId'))
		.appendTo(this.element);
		
		$.each(names, function(idx, value){
			$('<button id="' + that._getData('prefix') + value + '" style="width:20px;height:20px" ' +
					'class ="' + value + ' ui-icon ui-button ui-state-default ui-corner-all"/>')
			.click(callbacks[idx])
			.hover(
				function(){ 
					$(this).addClass("ui-state-hover"); 
				},
				function(){ 
					$(this).removeClass("ui-state-hover"); 
				}
			)
			.mousedown(function(){
				$(this).addClass("ui-state-active"); 
			})
			.mouseup(function(){
				$(this).removeClass("ui-state-active");
			})
			.appendTo($("#" + that._getData('prefix') + that._getData('imgId')));
		});
		this.setPosition(this._getData('top'), this._getData('left'));
	},
	setPosition: function(top, left){
		$("#" + this._getData('prefix') + this._getData('imgId'))
		.css("top", top + "px")
		.css("left", left + "px");
		this._setData('top', top);
		this._setData('left', left);
	},
	destroy: function() {
		var it = $("#" + this._getData('prefix') + this._getData('imgId'));
		
		it.hide("drop", {}, 500, function(){
			it.remove();
		});
		$.widget.prototype.destroy.apply(this, arguments);
	}
});

$.extend($.ui.toolbox, {
	version: "1.0",
	defaults: {
	    top: 0,
	    left: 0,
	    prefix: "",
	    imgId: "img",
		admin_img: [ 	 "ui-icon-wrench",
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
