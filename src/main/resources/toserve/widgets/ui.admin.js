(function($) {

$.widget("ui.admin", {
	_init: function (){
		var that = this;
		var parent = this._getData('parent');
	
		if(this._getData('prefix') == "")
			this._setData('prefix', this.element.attr("id") + "_");
		else
			this._setData('prefix', this._getData('prefix') + "_");
		
		$("<div></div>")
		.attr("style", "position:absolute;z-index:100;" + 
				"height:" 	+ this._getData('height') + "px;" +
				"width:"	+ this._getData('width') 	+ "px;" + 
				"top:" 		+ this._getData('top') 		+ "px;" +
				"left:" 	+ this._getData('left') 	+ "px;")
		.attr("id", this._getData('prefix') + this._getData('imgId'))
		.hover( function(){ 
					parent[that._getData('mouseOn')]();
				},
				function(){ 
					parent[that._getData('mouseOff')]();
				}
			)
		.draggable({
			drag: function(event, ui){
				$("#" + id).toolbox("setPosition",
					ui.position.top + (height - (5*20)), 
					ui.position.left + (width+4));
				parent[that._getData('drag')](ui.position.top, ui.position.left);
				that._setData('top', ui.position.top);
				that._setData('left', ui.position.left);
			}
		})
		.appendTo(this.element);
		
		this.element.toolbox({
			top: this._getData('top') + height - 5*20,
			left: this._getData('left') + (width+4),
			icon_images: parent._getData(this._getData('admin_img')),
			icon_callbacks: parent._getData(this._getData('admin_onClick'))
		});
		
	},
	setPosition: function(top, left){
		$("#" + this._getData('prefix') + this._getData('imgId'))
		.css("top", top + "px")
		.css("left", left + "px");
	},
	destroy: function() {
		this.element.toolbox("destroy");
		$("#" + this._getData('prefix') + this._getData('imgId')).remove();
		$.ui.admin.prototype.destroy.call(this);
	}
});

$.extend($.ui.admin, {
	version: "1.0",
	defaults: {
	    top: 0,
	    left: 0,
	    height: 300,
	    width: 212,
	    prefix: "",
	    imgId: "admin_mask",
	    mouseOn: "mouseOn",
	    mouseOff: "mouseOff",
	    drag: "drag",
	    admin_img: "admin_img",
	    admin_onClick: "admin_onClick",
	    parent: null
	}
});
})(jQuery);
