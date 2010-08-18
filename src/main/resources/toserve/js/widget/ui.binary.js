/** 
 * generates the a binary widget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.binary", {
  options: {
    version: "2.0",
    imgOn: "/classpath/images/lightbulb1.png",     //img in widget when value = true
    imgOff: "/classpath/images/lightbulb1off.png", //img in widget when value = false
    value: true,                              //true -> on; false -> off
    change:     function(){},                  //this callback is called on Click
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
    toolbox:         [ ["ui-icon-help",   "", ""],
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

		if(this.options.value)
			var img = this.options.imgOn;
		else
			var img = this.options.imgOff;

		this.element.simplewidget( $.extend({}, this.options, {
			createCopy: function(){
				return $('<div></div>').binary($.extend({}, that.options, {
				  isFavorite: true,
				  parentWidget: that.element
				}));
			}
		}));
		
		this.img = $("<img></img>")
		.css({
			position: 	"absolute",
			height: 	(this.options.height) + "px",
			width:		(this.options.width)  + "px",
			top: 		this.element.simplewidget('option', 'heightOffset') + "px",
			left: 		"0px"})
		.attr("src", img)
		.click(function (){
			if(!that.element.protowidget('option', 'isDragged'))
				eval(that._getOption('change'));
			else
				that.element.protowidget('option', 'isDragged', false);
		})
		.appendTo(this.element);
		
	},
	update_value: function(newValue){
		//updates UI
		if(newValue)
			this.img.attr("src", this.options.imgOn);
		else
			this.img.attr("src", this.options.imgOff);
		this._setOption('value', newValue);
	}
}); })(jQuery);
