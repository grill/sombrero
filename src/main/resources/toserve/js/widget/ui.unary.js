/** 
 * generates the a unary widget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.unary", {
  options: {
    version: "2.0",
    img: "/classpath/images/Toggle.png",
    change:     function(){},
    //-----------------------------------------------------
    //The following Options are inherited from simplewidget
    //-----------------------------------------------------
    height: 160,
    width: 160,
    top:                0,            
    left:               0, 
    heightOffset:       33,
    offset:             2,            
    stop:               "", //code is evaluated after dragging has stopped
    isHover:            true,              
    //This array determines the amount and the ability of
    //the items in the widget toolbar
    //1.: icon class; 2.: code that's evaluated after click on the respective icon
    //3.: URL that will be opened in a dialog after click
    toolbox: [ ["ui-icon-help",   "", ""],
               ["ui-icon-wrench", "", ""],
               ["ui-icon-trash",  "", ""],  
               ["ui-icon-minus",  "", ""] ],
    enterToolbox:         "",
    leaveToolbox:        "",
    text:               "",
    isDragged:          false,
    isAdminMode:        false,  //true -> admin mode - false -> normal mode
    adminSidebarTag:    "",
    isFavorite:         false,
    favoriteTag:        "",
    containment:        "",
    parentWidget:       "",   //id of parent widget
    active:             "",
    inactive:           "",
    cancel:             "",
    copy:               ""
  },
	_create: function (){
		var that = this;
	
		this.element.protowidget( $.extend({}, this.options, {
			createCopy: function(){
				return $('<div></div>').unary($.extend({}, that.options, {
          isFavorite: true,
          parentWidget: that.element
				}));
			}
		}));
		
		$("<img></img>")
		.css({
			position: 	"absolute",
			height: 	(this.options.height) + "px",
			width:		(this.options.width)  + "px",
			top: 		this.element.protowidget('option', 'heightOffset')+1 + "px",
			left: 		"0px"})
		.attr("id", "img")
		.attr("src", this.options.img)
		
		.click(function (){
			if(!that.element.protowidget('option', 'isDragged')){
				eval(that._getOption('change'));
			}else
				that.element.protowidget('option', 'isDragged', false);

		})
		.appendTo(this.element);
	}
}); })(jQuery);
