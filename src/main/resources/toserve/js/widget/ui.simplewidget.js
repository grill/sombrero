/** 
 * generates the a simplewidget
 * all documentation concerning properties has been done
 * in the http://github.com/lx9k/sombrero-thesis Diplomarbeit.pdf
 * @author Gabriel Grill 
 */

(function($) {

$.widget("ui.simplewidget", {
  options: {
    version:            "2.0",         //version number
    height:             160,            //height value of the widget
    width:              160,             //width value of the widget
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

    createCopy:         function(){},
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
    copy:               "",
    widgetViewMode:     false
  },
  _create: function (){
    var that = this;
    this.hoff = this.options.heightOffset;
    this.off = this.options.offset;

    if(this.options.widgetViewMode){
      this.options.left = 0;
      this.options.top = 0;
    }

    this.element
    .css({
      position: "absolute",
      border: "1px solid black",
      height: (this.options.height+this.hoff+this.off) + "px",
      width: (this.options.width+this.off) + "px",
      top: this.options.top + "px",
      left: this.options.left + "px"
    })
		    	
		if(this.options.isHover){
		  this.element
	    .attr("class", "ui-state-default ui-corner-all")
	    .hover(function(){ that.mouse_on(); },
	      function(){ that.mouse_off(); });
		}
		
    if(!this.options.widgetViewMode){
      this.toolbox();
      this.draggable();
    }
		this.titlebar();
	},
	mouse_on: function(){
		this.element.addClass("ui-state-hover");
	},
	mouse_off: function(){
		this.element.removeClass("ui-state-hover");
	},
	titlebar: function(){
		var that = this;
		
		this.element.titlebar({
		  top: 5,
    	left: 4,
    	width: this.options.width-8,
    	height: this.hoff,
    	text:	this.options.text,
    	isActive: this.options.isFavorite,
    	active: function(){
				eval(that.options.active);
			},
			inactive: function(){
			  eval(that.options.inactive);
			},
      widgetViewMode: that.options.widgetViewMode
		});
	},
	toolbox: function(){
		var that = this;
		var adm = $(that.options.adminSidebarTag);
    var top = this.element.css("top");
    var left = this.element.css("left");
		
		var toolboxFun = [
		  function(){ eval(that.options.toolbox[0][1]); },
			function(){ eval(that.options.toolbox[1][1]); },		//the associated icon of the admin widget
			function(){
				$('<div>Do you want to delete this widget?</div>').dialog({
					modal: true,
					title: "Delete",
					width: 400,
					buttons: { 
						"OK": function() {
							eval(that.options.toolbox[2][1]);
							that.element.remove();
							if(that.options.copy != "")
								$(that.options.favoriteTag).widgetcontainer('remove', $(that.options.copy));
							$(this).dialog("close"); 
						},
						"Cancel": function(){
							$(this).dialog("close");
						}
				}});
			},
			function(el){
				eval(that.options.toolbox[3][1]);
				if(el.hasClass('ui-icon-minus')){
					adm.widgetcontainer('append', that.element);
					el.removeClass('ui-icon-minus')
					  .addClass('ui-icon-plus');
					eval(that.options.enterToolbox);
			    top = that.element.css("top");
			    left = that.element.css("left");
				}else{
					adm.widgetcontainer('move', that.element);
					that.element
					.appendTo($(that.options.containment))
					.css({
						top:  top + "px",
						left: left + "px"
					});
					that.draggable();
					el.removeClass('ui-icon-plus')
					  .addClass('ui-icon-minus');
					eval(that.options.leaveToolbox);
				}
			}
		];
		
		var imgs = $.map(that.options.toolbox, function(el, idx){ return el[0]; });
		var helpUrls = $.map(that.options.toolbox, function(el, idx){ return el[2]; });
		
		if(this.options.isAdminMode)
	    	this.element.toolbox({
	    		img: imgs,
	    		onClick: toolboxFun,
	    		pheight: this.options.height+this.hoff+this.off,
	    		pwidth: this.options.width,
	    		helpUrl: helpUrls
	    	});
	},
	draggable: function(){
		var that = this;
		var isDragged = false;
		var ext = {};
		
		this.element.draggable({
			snap: true,
			snapMode: "both",
			snapTolerance: "10",
			cancel: this.options.cancel,
			stop: function(event, ui){
				eval(that.options.stop);
				that.options.isDragged = true;
			},
			collide: "block",
			containment: this.options.containment
		});
	},
	reactivate: function(){
		if(this.options.isAdminMode != null)
			this.toolbox();
		this.draggable();
	}
}) ;})(jQuery);
