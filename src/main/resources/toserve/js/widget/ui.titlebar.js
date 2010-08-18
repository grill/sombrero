/** 
 * generates the titlebar
 * @author Gabriel Grill 
 */


(function($) {

$.widget("ui.titlebar", {
  options: {
    version:     "2.0",
    width:       200,
    top:         100,
    left:        20,
    height:      300,
    text:        "",
    "active_?":  false,
    active:      function(){},
    inactive:    function(){},
    img:         "../../images/FavStar.PNG"
  },
	_create: function (){
		var that = this;
		
    	var container = $("<div></div>")
		.addClass("ui-corner-all")
		.addClass("ui-state-default")
		.css({
			position: 	"absolute",
			border: 	"1px solid black",
			height: 	this._getOption('height') + "px",
			width:		this._getOption('width') 	+ "px",
			top: 		  this._getOption('top') 	+ "px",
			left: 		this._getOption('left') 	+ "px"})
		.appendTo(this.element);
			
    	
		this.title = $("<div></div>")
		.css({
			position: 	"absolute",
			top: 		"6px",
			left: 		"5px"})
		.css("z-index", "2")
		.attr("id", "text")
		.html(this._getOption('text'))
		.appendTo(container);
		this.img = $('<img src="' + this._getOption('img') +
		    '" class=" ui-corner-all" style="position:absolute;top:2px;left:' +
		    ((this._getOption('left')+this._getOption('width'))-35) + 'px" />')
		.click(function(){
			/*if(that._getOption('active_?')){
				$(this)
				.removeClass("ui-state-default");
				that._setOption('active_?', false);
				that._getOption('inactive')();
			} else {
				$(this)
				.addClass("ui-state-default");
				that._setOption('active_?', true);
				that._getOption('active')();
			}*/
		  that.setFav(that._getOption('active_?'));
		})
		.appendTo(container);
		
    	if(this._getOption('active_?'))
    		this.img.addClass("ui-state-default");
	},
	click: function(){
		this.img.click();
	},
	setFav: function(b){
		if(b){
			this.img
			.addClass("ui-state-default");
			that._setOption('active_?', true);
		}else{
			this.img
			.removeClass("ui-state-default");
			this._setOption('active_?', false);
		}
	},
	update_title: function(s) {
		this.title.html(s);
	}
}); })(jQuery);
