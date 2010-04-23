/*-------------------------------------------------------------------- 
Scripts for creating and manipulating custom menus based on standard <ul> markup
Version: 3.0, 03.31.2009

By: Maggie Costello Wachs (maggie@filamentgroup.com) and Scott Jehl (scott@filamentgroup.com)
	http://www.filamentgroup.com
	* reference articles: http://www.filamentgroup.com/lab/jquery_ipod_style_drilldown_menu/
		
Copyright (c) 2009 Filament Group
Dual licensed under the MIT (filamentgroup.com/examples/mit-license.txt) and GPL (filamentgroup.com/examples/gpl-license.txt) licenses.
--------------------------------------------------------------------*/


/*
 And rewritten by Gabriel Grill for sombrero
*/


(function($) {

$.widget("ui.ipod", {
	_init: function (){
		var that = this;
		var container = $('<div id="ein" style="top:' + this._getData('top') + 'px;left:' + this._getData('left') + 'px;" class="fg-menu-container ui-widget ui-widget-content ui-corner-all" >' + this.element.html() + '</div>');
		container.css({ width: this.options.width }).appendTo(this._getData('parent')).find('ul:first').not('.fg-menu-breadcrumb').addClass('fg-menu');
		container.find('ul, li a').addClass('ui-corner-all');

		// aria roles & attributes
		container.find('ul').attr('role', 'menu').eq(0).attr('aria-activedescendant','active-menuitem').attr('aria-labelledby', "Label Hello");
		container.find('li').attr('role', 'menuitem');
		container.find('li:has(ul)').attr('aria-haspopup', 'true').find('ul').attr('aria-expanded', 'false');
		container.find('a').attr('tabindex', '-1');
		
		//container.appendTo($("#help"));
		if (container.find('ul').size() > 1) {
			var topList = container.find('.fg-menu');	
			var breadcrumb = $('<ul class="fg-menu-breadcrumb ui-widget-header ui-corner-all ui-helper-clearfix"></ul>');
			var crumbDefaultHeader = $('<li class="fg-menu-breadcrumb-text">'+this.options.crumbDefaultText+'</li>');
			var firstCrumbText = (this.options.backLink) ? this.options.backLinkText : this.options.topLinkText;
			var firstCrumbClass = (this.options.backLink) ? 'fg-menu-prev-list' : 'fg-menu-all-lists';
			var firstCrumbLinkClass = (this.options.backLink) ? 'ui-state-default ui-corner-all' : '';
			var firstCrumbIcon = (this.options.backLink) ? '<span class="ui-icon ui-icon-triangle-1-w"></span>' : '';
			var firstCrumb = $('<li class="'+firstCrumbClass+'"><a href="#" class="'+firstCrumbLinkClass+'">'+firstCrumbIcon+firstCrumbText+'</a></li>');
			var depth = 0;
			var blen = 30;
			var isAnimatingBack = false;
			
			container.addClass('fg-menu-ipod');
			if (this.options.backLink) { breadcrumb.addClass('fg-menu-footer').appendTo(container).hide(); }
			else { breadcrumb.addClass('fg-menu-header').prependTo(container); };
			breadcrumb.append(crumbDefaultHeader);
			
			var checkMenuHeight = function(el){
				if (container.height() > that.options.maxHeight) { container.addClass('fg-menu-scroll') };	
				container.css({ height: that.options.maxHeight });
				if (el.height() > that.options.maxHeight) { el.addClass('fg-menu-scroll') };	
				el.css({ height: that.options.maxHeight-blen });
			};
			
			var resetChildMenu = function(el){ el.removeClass('fg-menu-scroll').removeClass('fg-menu-current').height('auto'); };
			
			topList
			.addClass('fg-menu-content fg-menu-current ui-widget-content ui-helper-clearfix')
			.css({ width: container.width() })
			.find('ul')
				.css({ width: container.width(), left: container.width() })
				.addClass('ui-widget-content')
				.hide();		
			checkMenuHeight(topList);
			
			topList.find('a').each(function(){
				// if the link opens a child menu:
				if ($(this).next().children().is('li')) {
					$(this)
						.addClass('fg-menu-indicator')
						.each(function(){
							$(this).html('<span>' + $(this).text() + '</span><span style="z-index:200" class="fg-border ui-icon ' + that.options.nextMenuLink + '"><span>df</span></span>');
							$(this).attr('style', 'z-index:1');
						});
						/*.click(function(){ // ----- show the next menu			
							var nextList = $(this).next();
				    		var parentUl = $(this).parents('ul:eq(0)');   		
				    		var parentLeft = (parentUl.is('.fg-menu-content')) ? 0 : parseFloat(topList.css('left'));    		
				    		var nextLeftVal = Math.round(parentLeft - parseFloat(container.width()));
				    		var footer = $('.fg-menu-footer');

				    		footer.css("position", "absolute").css("top", that.options.maxHeight-blen+"px")
				    		.css("width", container.width()-10);
							depth = depth + 1;
				    		// show next menu
				    		resetChildMenu(parentUl);
				    		checkMenuHeight(nextList);
							topList.animate({ left: nextLeftVal }, that.options.crossSpeed);						
				    		nextList.show().addClass('fg-menu-current').attr('aria-expanded', 'true');    
				    		
				    		var setPrevMenu = function(backlink){
				    			var b = backlink;
				    			var c = $('.fg-menu-current');
					    		var prevList = c.parents('ul:eq(0)');
					    		c.hide().attr('aria-expanded', 'false');
				    			resetChildMenu(c);
				    			checkMenuHeight(prevList);
					    		prevList.addClass('fg-menu-current').attr('aria-expanded', 'true');
					    		if (prevList.hasClass('fg-menu-content')) { b.remove(); footer.hide(); };
				    		};		
				
							// initialize "back" link
							if (that.options.backLink) {
								if (footer.find('a').size() == 0) {
									footer.show();
									$('<a href="#"><span class="ui-icon ui-icon-triangle-1-w"></span> <span>Back</span></a>')
										.appendTo(footer)
										.click(function(){ // ----- show the previous menu
											if(depth > 0 && !isAnimatingBack){
												isAnimatingBack = true;
												depth = depth - 1;
												var b = $(this);
												var prevLeftVal = parseFloat(topList.css('left')) + container.width();		    						    		
												topList.animate({ left: prevLeftVal },  that.options.crossSpeed, function(){
													setPrevMenu(b);
													isAnimatingBack = false;
													//alert("hi");
												});
											}
											return false;
										});
								}
							}
							// or initialize top breadcrumb
				    		else { 
				    			if (breadcrumb.find('li').size() == 1){				
									breadcrumb.empty().append(firstCrumb);
									firstCrumb.find('a').click(function(){
										menu.resetDrilldownMenu();
										return false;
									});
								}
								$('.fg-menu-current-crumb').removeClass('fg-menu-current-crumb');
								var crumbText = $(this).find('span:eq(0)').text();
								var newCrumb = $('<li class="fg-menu-current-crumb"><a href="javascript://" class="fg-menu-crumb">'+crumbText+'</a></li>');	
								newCrumb
									.appendTo(breadcrumb)
									.find('a').click(function(){
										if ($(this).parent().is('.fg-menu-current-crumb')){
											menu.chooseItem(this);
										}
										else {
											var newLeftVal = - ($('.fg-menu-current').parents('ul').size() - 1) * 180;
											topList.animate({ left: newLeftVal }, this.options.crossSpeed, function(){
												setPrevMenu();
											});
										
											// make this the current crumb, delete all breadcrumbs after this one, and navigate to the relevant menu
											$(this).parent().addClass('fg-menu-current-crumb').find('span').remove();
											$(this).parent().nextAll().remove();									
										};
										return false;
									});
								newCrumb.prev().append(' <span class="ui-icon '+this.options.nextCrumbLink+'"></span>');
				    		};			
				    		return false;    		
		    			});*/
				}
				// if the link is a leaf node (doesn't open a child menu)
				else {
					/*$(this).click(function(){
						menu.chooseItem(this);
						return false;
					});*/
				};
			});
		}
		if (this.options.linkHover) {
			var allLinks = container.find('.fg-menu li a');
			allLinks.hover(
				function(){
					var menuitem = $(this);
					$('.'+that.options.linkHover).removeClass(that.options.linkHover).blur().parent().removeAttr('id');
					$(this).addClass(that.options.linkHover).parent().attr('id','active-menuitem');
					//$('#foc').focus();//.focus()
				},
				function(){
					$(this).removeClass(that.options.linkHover).blur().parent().removeAttr('id');
				}
			);			
			var allLinks = container.find('.fg-menu li a span.fg-border');
			allLinks.hover(
					function(){
						var menuitem = $(this).children();
						//$('.'+that.options.linkHover).removeClass(that.options.linkHover).blur().parent().removeAttr('id');
						
						//alert(menuitem.html());
						
						menuitem
						//.addClass(that.options.linkHover)
						.addClass('ui-corner-all')
						.attr('style', 'width:14px;height:14px;border: 1px solid black;z-index:100');
						$(this).parent().attr('id','active-menuitem');
						//$('#foc').focus();//.focus()
					},
					function(){
						var menuitem = $(this).children();
						
						menuitem
						.removeClass('ui-corner-all')
						.attr('style', 'z-index:100')
						$(this).removeClass(that.options.linkHover).blur().parent().removeAttr('id');
					}
				)
				.click(function(){
					var nextList = $(this).parent().next();
		    		var parentUl = $(this).parent().parents('ul:eq(0)');   		
		    		var parentLeft = (parentUl.is('.fg-menu-content')) ? 0 : parseFloat(topList.css('left'));    		
		    		var nextLeftVal = Math.round(parentLeft - parseFloat(container.width()));
		    		var footer = $('.fg-menu-footer');

		    		footer.css("position", "absolute").css("top", that.options.maxHeight-blen+"px")
		    		.css("width", container.width()-10);
					depth = depth + 1;
		    		// show next menu
		    		resetChildMenu(parentUl);
		    		checkMenuHeight(nextList);
					topList.animate({ left: nextLeftVal }, that.options.crossSpeed);						
		    		nextList.show().addClass('fg-menu-current').attr('aria-expanded', 'true');    
		    		
		    		var setPrevMenu = function(backlink){
		    			var b = backlink;
		    			var c = $('.fg-menu-current');
			    		var prevList = c.parents('ul:eq(0)');
			    		c.hide().attr('aria-expanded', 'false');
		    			resetChildMenu(c);
		    			checkMenuHeight(prevList);
			    		prevList.addClass('fg-menu-current').attr('aria-expanded', 'true');
			    		if (prevList.hasClass('fg-menu-content')) { b.remove(); footer.hide(); };
		    		};		
		
					// initialize "back" link
					if (that.options.backLink) {
						if (footer.find('a').size() == 0) {
							footer.show();
							$('<a href="#"><span class="ui-icon ui-icon-triangle-1-w"></span> <span>Back</span></a>')
								.appendTo(footer)
								.click(function(){ // ----- show the previous menu
									if(depth > 0 && !isAnimatingBack){
										isAnimatingBack = true;
										depth = depth - 1;
										var b = $(this);
										var prevLeftVal = parseFloat(topList.css('left')) + container.width();		    						    		
										topList.animate({ left: prevLeftVal },  that.options.crossSpeed, function(){
											setPrevMenu(b);
											isAnimatingBack = false;
											//alert("hi");
										});
									}
									return false;
								});
						}
					}
					// or initialize top breadcrumb
		    		else { 
		    			if (breadcrumb.find('li').size() == 1){				
							breadcrumb.empty().append(firstCrumb);
							firstCrumb.find('a').click(function(){
								menu.resetDrilldownMenu();
								return false;
							});
						}
						$('.fg-menu-current-crumb').removeClass('fg-menu-current-crumb');
						var crumbText = $(this).find('span:eq(0)').text();
						var newCrumb = $('<li class="fg-menu-current-crumb"><a href="javascript://" class="fg-menu-crumb">'+crumbText+'</a></li>');	
						newCrumb
							.appendTo(breadcrumb)
							.find('a').click(function(){
								if ($(this).parent().is('.fg-menu-current-crumb')){
									menu.chooseItem(this);
								}
								else {
									var newLeftVal = - ($('.fg-menu-current').parents('ul').size() - 1) * 180;
									topList.animate({ left: newLeftVal }, this.options.crossSpeed, function(){
										setPrevMenu();
									});
								
									// make this the current crumb, delete all breadcrumbs after this one, and navigate to the relevant menu
									$(this).parent().addClass('fg-menu-current-crumb').find('span').remove();
									$(this).parent().nextAll().remove();									
								};
								return false;
							});
						newCrumb.prev().append(' <span class="ui-icon '+this.options.nextCrumbLink+'"></span>');
		    		};			
		    		return false;
				});
		};
		
		if (this.options.linkHoverSecondary) {
			container.find('.fg-menu li').hover(
				function(){
					$(this).siblings('li').removeClass(that.options.linkHoverSecondary);
					if (that.options.flyOutOnState) { $(this).siblings('li').find('a').removeClass(that.options.flyOutOnState); }
					$(this).addClass(that.options.linkHoverSecondary);
				},
				function(){ $(this).removeClass(that.options.linkHoverSecondary); }
			);
		};
		
		var nextMenu = function(element){
			var nextList = element.next();
    		var parentUl = element.parents('ul:eq(0)');   		
    		var parentLeft = (parentUl.is('.fg-menu-content')) ? 0 : parseFloat(topList.css('left'));    		
    		var nextLeftVal = Math.round(parentLeft - parseFloat(container.width()));
    		var footer = $('.fg-menu-footer');

    		footer.css("position", "absolute").css("top", that.options.maxHeight-blen+"px")
    		.css("width", container.width()-10);
			depth = depth + 1;
    		// show next menu   		
    		resetChildMenu(parentUl);
    		checkMenuHeight(nextList);
			//topList.animate({ left: nextLeftVal }, that.options.crossSpeed);		
    		topList.css('left', nextLeftVal);				
    		nextList.show().addClass('fg-menu-current').attr('aria-expanded', 'true');    
    		
    		var setPrevMenu = function(backlink){
    			var b = backlink;
    			var c = $('.fg-menu-current');
	    		var prevList = c.parents('ul:eq(0)');
	    		c.hide().attr('aria-expanded', 'false');
    			resetChildMenu(c);
    			checkMenuHeight(prevList);
	    		prevList.addClass('fg-menu-current').attr('aria-expanded', 'true');
	    		if (prevList.hasClass('fg-menu-content')) { b.remove(); footer.hide(); };
    		};		

			// initialize "back" link
			if (that.options.backLink) {
				if (footer.find('a').size() == 0) {
					footer.show();
					$('<a href="#"><span class="ui-icon ui-icon-triangle-1-w"></span> <span>Back</span></a>')
						.appendTo(footer)
						.click(function(){ // ----- show the previous menu
							if(depth > 0 && !isAnimatingBack){
								isAnimatingBack = true;
								depth = depth - 1;
								var b = $(this);
								var prevLeftVal = parseFloat(topList.css('left')) + container.width();		    						    		
								topList.animate({ left: prevLeftVal },  that.options.crossSpeed, function(){
									setPrevMenu(b);
									isAnimatingBack = false;
									//alert("hi");
								});
							}
							return false;
						});
				}
			}
			// or initialize top breadcrumb
    		else { 
    			if (breadcrumb.find('li').size() == 1){				
					breadcrumb.empty().append(firstCrumb);
					firstCrumb.find('a').click(function(){
						menu.resetDrilldownMenu();
						return false;
					});
				}
				$('.fg-menu-current-crumb').removeClass('fg-menu-current-crumb');
				var crumbText = $(this).find('span:eq(0)').text();
				var newCrumb = $('<li class="fg-menu-current-crumb"><a href="javascript://" class="fg-menu-crumb">'+crumbText+'</a></li>');	
				newCrumb
					.appendTo(breadcrumb)
					.find('a').click(function(){
						if ($(this).parent().is('.fg-menu-current-crumb')){
							menu.chooseItem(this);
						}
						else {
							var newLeftVal = - ($('.fg-menu-current').parents('ul').size() - 1) * 180;
							topList.animate({ left: newLeftVal }, this.options.crossSpeed, function(){
								setPrevMenu();
							});
						
							// make this the current crumb, delete all breadcrumbs after this one, and navigate to the relevant menu
							$(this).parent().addClass('fg-menu-current-crumb').find('span').remove();
							$(this).parent().nextAll().remove();									
						};
						return false;
					});
				newCrumb.prev().append(' <span class="ui-icon '+this.options.nextCrumbLink+'"></span>');
    		};
		};
		
		$.each(this.options.initPath, function(idx, val){
			nextMenu($(val + '.fg-menu-indicator'));
		});
	},
	destroy: function() {
		
		$.ui.ipod.prototype.destroy.call(this);
	},
	setPosition: function(top, left){
		
	}, 
	resetDrilldownMenu: function(){
		$('.fg-menu-current').removeClass('fg-menu-current');
		topList.animate({ left: 0 }, this.options.crossSpeed, function(){
			$(this).find('ul').each(function(){
				$(this).hide();
				resetChildMenu($(this));				
			});
			topList.addClass('fg-menu-current');			
		});		
		$('.fg-menu-all-lists').find('span').remove();	
		breadcrumb.empty().append(crumbDefaultHeader);		
		$('.fg-menu-footer').empty().hide();	
		checkMenuHeight(topList);		
	}
});


$.extend($.ui.ipod, {
	version: "1.0",
	defaults: {
		width: 200,
	    top: 0,
	    left: 0,
		maxHeight: 300, // max height of menu (if a drilldown: height does not include breadcrumb)
		showSpeed: 200, // show/hide speed in milliseconds
		callerOnState: 'ui-state-active', // class to change the appearance of the link/button when the menu is showing
		loadingState: 'ui-state-loading', // class added to the link/button while the menu is created
		linkHover: 'ui-state-hover', // class for menu option hover state
		linkHoverSecondary: 'li-hover', // alternate class, may be used for multi-level menus		
									// ----- multi-level menu defaults -----
		crossSpeed: 200, // cross-fade speed for multi-level menus
		crumbDefaultText: '',
		backLink: true, // in the ipod-style menu: instead of breadcrumbs, show only a 'back' link
		backLinkText: 'Back',
		flyOut: false, // multi-level menus are ipod-style by default; this parameter overrides to make a flyout instead
		flyOutOnState: 'ui-state-default',
		nextMenuLink: 'ui-icon-triangle-1-e', // class to style the link (specifically, a span within the link) used in the multi-level menu to show the next level
		topLinkText: 'All',
		nextCrumbLink: 'ui-icon-carat-1-e',
		initPath: [],
		parent: "#col1_content"
	}
	});
})(jQuery);
