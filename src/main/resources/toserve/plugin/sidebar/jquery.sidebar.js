/*!
 * Side Bar v1.0.1
 * http://sideroad.secret.jp/
 *
 * Copyright (c) 2009 sideroad
 *
 * Dual licensed under the MIT licenses.
 * Date: 2009-09-01
 */

/*
 and heavily rewritten by Gabriel Grill for Sombrero
*/

//Side Bar Plugin
(function($) {
		
	$.fn.sidebar = function(options){
		return this.each(function(){
			options = options || {};
			
			//default setting
			options = $.extend({
				position: "left",
				width: 100,
				height: 200,
				pheight: 400,
				injectWidth: 50,
				top: null,
				ptop: null,
				liMouseOver: {
					marginLeft: "5px"
				},
				liMouseOut: {
					marginLeft: "0px"
				},
				open : "click",
				close : "click"
			}, options);
			var m;
			var pcss = {
	                height: options.pheight,
	                width: options.width
			};
			var icss;
            var ccss = {
                height: options.height,
                width: options.injectWidth+5
            };
			if(options.position == "left" || options.position == "right") {
                m = options.injectWidth;
				icss = {
					height: options.height,
					width: options.injectWidth
				};
				if(options.ptop != null)
					pcss.top = options.ptop;
				if(options.top != null){
					ccss.top = options.top;
				}else
					ccss.top = ($(window).height()/2) - (options.height/2) + "px";
				
			} else {
				m = options.height - options.injectWidth;
                icss = {
                    height: options.injectWidth,
                    width: options.width
                };
                ccss.left = ($(window).width()/2) - (options.width/2) + "px";
			}
			var bcss = {
				height: options.height,
				width: options.width
			};
			var e = {};
			var l = {};
			var pe = {};
			var pl = {};
			
			ccss[options.position] =  "-" + 5 + "px";
			pcss[options.position] =  "-" + options.width + "px";
			icss[options.position] = m + "px";
			e[options.position] = options.width-15;
			pe[options.position] = -5;
			l[options.position] = 0;
			pl[options.position] = "-" + options.width + "px";
			
			var p = $("<div><div/>").attr("id", "jquerySideBar" + new Date().getTime()).addClass("sidebar-container-" + options.position).css(pcss).css({"z-index": "9999"});
			//container
			var c = $("<div><div/>").attr("id", "jquerySideBar" + new Date().getTime()).addClass("sidebar-container-" + options.position).css(ccss).css({"z-index": "9998"});
			
			//inject
			var i = $("<div><div/>").addClass("sidebar-inject-" + options.position).css(icss).css({"z-index": "9999"});
			
			//body
			var b = $("<div><div/>").addClass("sidebar-body").css(bcss)/*.hide()*/;
			
			//menu events
            var isEnter;
            var isOut = false;
			c.addClass("sidebar-menu").find("li,li *").click(function(){
                if (!isEnter || isOut) return;
                if(!isOut){
                	isOut = true;
                	$(this).animate(options.liMouseOver, 250);
                }else{
                	isOut = false;
                	$(this).animate(options.liMouseOut, 250);
                }
            });
			
			/*$("body").click(function(){
				if(!isEnter) return;
				if(isProcessing) return;
				isProcessing = true;
				c.animate(l, {
					duration: 200,
					complete: function(){
						//b.hide("clip", 200, function(){
							i.fadeIn(200, function(){
                                isEnter = false;
								isProcessing = false;
							});
						//});
					}
				});				
				p.animate(pl, {
					duration: 200,
					complete: function(){}
				});
			});*/
			
			//container events
			var isProcessing;
			c.bind(options.open,function(){
				if (isEnter) return;
				if (isProcessing) return;
				isEnter = true;
                isProcessing = true;
				p.animate(pe, {
					duration: 200,
					complete: function(){
					}
				});
				c.animate(e, {
					duration: 200,
					complete: function(){
					i.fadeOut(200, function(){
							/*b.show("clip", 200,function(){
                                isProcessing = false;
							});*/
                                isProcessing = false;
                                i.removeClass("sidebar-inject-right");
                                i.addClass("sidebar-inject-left");
								i.fadeIn(200, function(){});
						});
					}
				});
			}).bind(options.close,function(){
				if(!isEnter) return;
				if(isProcessing) return;
				isProcessing = true;
				c.animate(l, {
					duration: 200,
					complete: function(){
						//b.hide("clip", 200, function(){
					i.fadeOut(200, function(){

	                    i.removeClass("sidebar-inject-left");
	                    i.addClass("sidebar-inject-right");
						i.fadeIn(200, function(){});
									i.fadeIn(200, function(){
		                                isEnter = false;
										isProcessing = false;
									});
					});
						//});
					}
				});				
				p.animate(pl, {
					duration: 200,
					complete: function(){}
				});
			});
			
			//append to body
			b.append(this);
			p.append(b);
			c.append(i);
			$(document.body).append(c);
			$(document.body).append(p);
			p.css({opacity: 1});
			/*$(window).resize(function(){
	            if(options.position == "left" || options.position == "right") {
					c.css({top:($(this).height()/2) - (options.height/2) + "px"});
	            } else {
	                c.css({left:($(this).width()/2) - (options.width/2) + "px"});
	            }
			});*/
		});
	}
})(jQuery);