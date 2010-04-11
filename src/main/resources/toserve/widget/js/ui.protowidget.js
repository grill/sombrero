(function($) {

$.widget("ui.protowidget", {
	_init: function (){
		var that = this;
		this.hoff = this._getData('hoff');
		this.off = this._getData('off');
	
		this.element
		.attr("class", "ui-state-default ui-corner-all")
		.css({
			position: 	"absolute",
			border: 	"1px solid black",
			height: 	(this._getData('height')+this.hoff+this.off) 	+ "px",
			width:		(this._getData('width')+this.off) 		+ "px",
			top: 		this._getData('top') 				+ "px",
			left: 		this._getData('left') 				+ "px"})
		.hover(function(){ that.mouseOn(); },
				function(){ that.mouseOff(); });
		    	
		if(this._getData('hoveroff')){
			this.element
			.removeClass('ui-state-default')
			.removeClass('ui-corner-all');
		}
		
		this.toolbox();
		this.draggable();
		this.titlebar();
	},
	mouseOn: function(){
		if(!this._getData('hoveroff')) this.element.addClass("ui-state-hover");
	},
	mouseOff: function(){
		if(!this._getData('hoveroff')) this.element.removeClass("ui-state-hover");
	},
	titlebar: function(){
		var that = this;
		var fav = that._getData('favorites');
		
    	this.element.titlebar({
			top: 5,
			left: 4,
			width: this._getData('width')-8,
			height: this.hoff,
			text:	this._getData('text'),
			is_active: this._getData('is_active'),
			active: function(){
				if(fav != null)
					//if(that._getData('admin')){
						//fav.favorites('append', that.element.detach());
					//}else{
						that._setData('copy', that._getData('createCopy')());
						fav.favorites('append', that._getData('copy').detach());
					//}
				that._getData('active')();
			},
			inactive: function(){
				if(fav != null)
					if(that._getData('copy') != null)
						fav.favorites('remove', that._getData('copy'));
					else{
						if(that._getData('parentTag') != null)
							/*if(that._getData('admin')){
								fav.favorites('remove', that.element);
								that.element
								.appendTo(that._getData('parentTag'))
								.css({
									top: that._getData('top') + "px",
									left: that._getData('left') + "px"
								});
								that.reactivate();
							}else{*/
								if(that._getData('parentObj') != null)
									that._getData('parentObj').titlebar('click');
							//}
						else
							fav.favorites('remove', that.element);
					}
				that._getData('inactive')();
			}
    	});
	},
	toolbox: function(){
		var that = this;
		var adm = that._getData('admin');
		var fav = that._getData('favorites');
		
		var admin_fun = [
		    function(){
		    	that._getData('admin_onClick')[0]();
			},		//callback is called on the click on
			function(){
				that._getData('admin_onClick')[1]();
			},		//the associated icon of the admin widget
			function(){
				$('<div>Do you want to delete this widget?</div>').dialog({
					modal: true,
					title: "Delete",
					width: 400,
					buttons: { 
						"OK": function() {
							that._getData('admin_onClick')[2]();
							that.element.remove();
							if(that._getData('copy') != null)
								fav.favorites('remove', that._getData('copy'));
							$(this).dialog("close"); 
						},
						"Cancel": function(){
							$(this).dialog("close");
						}
				}});
			},
			function(el){
				that._getData('admin_onClick')[3]();
				if(el.hasClass('ui-icon-minus')){	// has class
					//that.element.remove();
					adm.favorites('append', that.element);
					//that.element.toolbox('reactivate');
					//that.toolbox();
					//that.reactivate();
					el.removeClass('ui-icon-minus')
					.addClass('ui-icon-plus');
					that._getData("in_toolbox")();
				}else{
					adm.favorites('move', that.element);
					that.element
					.appendTo(that._getData('parentTag'))
					.css({
						top: that._getData('top') + "px",
						left: that._getData('left') + "px"
					});
					//that.reactivate();
					el
					.removeClass('ui-icon-plus')
					.addClass('ui-icon-minus');
					that._getData("out_toolbox")();
				}
			}
		];

		if(this._getData('admin') != null)
	    	this.element.toolbox({
	    		admin_img: this._getData('admin_img'),
				admin_onClick: admin_fun,
				pheight: this._getData('height')+this.hoff+this.off,
				pwidth: this._getData('width'),
				admin: this._getData('admin'),
				admin_url: this._getData('admin_url')
			});
	},
	draggable: function(){
		var that = this;
		var isDragged = false;
		var ext = {};
		
		if(this._getData('cancel') != "")
			ext = {cancel: this._getData('cancel')};
		this.element.draggable($.extend(ext, {
			snap: true,
			snapMode: "both",
			snapTolerance: "10",
			cancel: this._getData('cancel'),
			stop: function(event, ui){
				that._setData('top', ui.position.top);
				that._setData('left', ui.position.left);
				that._getData('stop')();
				that._setData('isDragged', true);
			},
			collide: "block",
			containment: this._getData('parentTag')//,
			//revert: 'valid'
			//cancel: "div:not(#" + this.element.attr("id") + ")"
		}));
		/*this.element.droppable({
			accept: ".ui-draggable",
			tolerance: "touch"//,
			//drop: function(event, ui){
			//}
		});*/
	},
	reactivate: function(){
		if(this._getData('admin') != null)
			this.toolbox();
		this.draggable();
	},
	destroy: function() {
		//this.element.detach();
		//$.ui.protowidget.prototype.destroy.call(this);
	},
	update_title: function(s) {
		this.element.titlebar('update_title', s);
	}
});

$.extend($.ui.protowidget, {
	version: "1.0",							//version number
	defaults: {
	    top: 0,								//top value of the widget
	    left: 0,							//left value of the widget
		height: 200,						//height value of the widget
		width: 200,							//width value of the widget
	    stop: function(){},					//callback is called when drag stopped
	    prefix: "proto",							//prefix for the id atribute
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
	    admin: null,					//true -> admin mode - false -> normal mode
	    createCopy: function(){},
	    isDragged: false,
	    hoff: 33,
	    off: 2,
	    parentTag: null,
	    parentObj: null,
		active: 	   function(){},
		inactive: 	   function(){},
		cancel: "",
		admin_url: 		[ "",
		           		  "" ],
		copy: 			null,
		hoveroff:		false,
		in_toolbox:		function(){},
		out_toolbox:	function(){}
	}
});
})(jQuery);
