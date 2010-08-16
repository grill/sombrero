/** 
 * generates the titlebar
 * @author Gabriel Grill 
 */


(function($) {

$.widget("ui.titlebar", {
	_init: function (){
		var active = this._getData('is_active');
		var that = this;
		
    	var container = $("<div></div>")
		.addClass("ui-corner-all")
		.addClass("ui-state-default")
		.css({
			position: 	"absolute",
			border: 	"1px solid black",
			height: 	this._getData('height') + "px",
			width:		this._getData('width') 	+ "px",
			top: 		this._getData('top') 	+ "px",
			left: 		this._getData('left') 	+ "px"})
		.appendTo(this.element);
			
    	
		this.title = $("<div></div>")
		.css({
			position: 	"absolute",
			top: 		"6px",
			left: 		"5px"})
		.css("z-index", "2")
		.attr("id", this._getData('prefix') + "text")
		.html(this._getData('text'))
		.appendTo(container);
		this.img = $('<img src="' + this._getData('img') + '" class=" ui-corner-all" style="position:absolute;top:2px;left:' + ((this._getData('left')+this._getData('width'))-35) + 'px" />')
		.click(function(){
			if(that._getData('is_active')){
				$(this)
				.removeClass("ui-state-default");
				that._setData('is_active', false);
				that._getData('inactive')();
			} else {
				$(this)
				.addClass("ui-state-default");
				that._setData('is_active', true);
				that._getData('active')();
			}
		})
		.appendTo(container);
		
    	if(this._getData('is_active'))
    		this.img.addClass("ui-state-default");
	}/*,
	destroy: function() {
		//$.ui.titlebar.prototype.destroy.call(this);
	}*/,
	click: function(){
		this.img.click();
	},
	setFav: function(b){
		if(b){
			$(this)
			.addClass("ui-state-default");
			that._setData('is_active', true);
		}else{
			this.img
			.removeClass("ui-state-default");
			this._setData('is_active', false);
		}
	},
	update_title: function(s) {
		this.title.html(s);
	}
});


$.extend($.ui.titlebar, {
	version: 		   "1.0",
	defaults: {
		width: 		   200,
	    top: 		   100,
	    left: 		   20,
		height:		   300,
	    prefix: 	   "",
		text: 		   "",
		is_active: 	   false,
		active: 	   function(){},
		inactive: 	   function(){},
		img: 		   "/images/FavStar.PNG"
	}
});
})(jQuery);
