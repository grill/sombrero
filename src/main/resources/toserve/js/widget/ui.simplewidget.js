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
    copy:               ""
  },
  _create: function (){
    var that = this;
    this.hoff = this.options.heightOffset;
    this.off = this.options.offset;

    this.element
    .css({
      position: "absolute",
      border: "1px solid black",
      height: (this.options.height+this.hoff+this.off) + "px",
      width: (this.options.width+this.off) + "px",
      top: this.options.top + "px",
      left: this._getOptoin('left') + "px"
    })
		    	
		if(this.options.isHover){
		  this.element
	    .attr("class", "ui-state-default ui-corner-all")
	    .hover(function(){ that.mouse_on(); },
	      function(){ that.mouse_off(); });
		}
		
		this.toolbox();
		this.draggable();
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
				eval(that._getOption('active'));
			},
			inactive: function(){
			  eval(that._getOption('inactive'));
			}
		});
	},
	toolbox: function(){
		var that = this;
		var adm = that._getOption('adminMode');
    var top = this.element.css("top");
    var left = this.element.css("left");
		
		var toolboxFun = [
		  function(){ eval(that._getOption('toolbox')[0][1]); },
			function(){ eval(that._getOption('toolbox')[1][1]); },		//the associated icon of the admin widget
			function(){
				$('<div>Do you want to delete this widget?</div>').dialog({
					modal: true,
					title: "Delete",
					width: 400,
					buttons: { 
						"OK": function() {
							eval(that._getOption('toolbox')[2][1]);
							that.element.remove();
							if(that._getOption('copy') != "")
								$(that._getOption("favoriteTag")).favorites('remove', $(that._getOption('copy')));
							$(this).dialog("close"); 
						},
						"Cancel": function(){
							$(this).dialog("close");
						}
				}});
			},
			function(el){
				eval(that._getOption('toolbox')[3][1]);
				if(el.hasClass('ui-icon-minus')){
					adm.favorites('append', that.element);
					el.removeClass('ui-icon-minus')
					  .addClass('ui-icon-plus');
					eval(that._getOption("enterToolbox"));
			    top = that.element.css("top");
			    left = that.element.css("left");
				}else{
					adm.favorites('move', that.element);
					that.element
					.appendTo($(that._getOption('containment')))
					.css({
						top:  top + "px",
						left: left + "px"
					});
					that.draggable();
					el.removeClass('ui-icon-plus')
					  .addClass('ui-icon-minus');
					eval(that._getOption("leaveToolbox"));
				}
			}
		];
		
		var imgs = $.map(that._getOption("toolbox"), function(el, idx){ el[0]; });
		var urls = $.map(that._getOption("toolbox"), function(el, idx){ el[2]; });
		
		if(this._getData('admin_mode'))
	    	this.element.toolbox({
	    		img: imgs,
	    		onClick: toolboxFun,
	    		pheight: this.options.height+this.hoff+this.off,
	    		pwidth: this.options.width,
	    		adminSidebarTag: this.options.adminSidebarTag,
	    		url: urls
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
				eval(that._getOption('stop'));
				that._setOption('isDragged', true);
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
