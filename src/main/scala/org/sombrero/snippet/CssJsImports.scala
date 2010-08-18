package org.sombrero.snippet

class CssJsImports {
  def render(xhtml : NodeSeq) : NodeSeq = {
    <head>
      <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
      <title>Sombrero</title>
      
      <link href="/classpath/css/layout/default-user/menu.css" rel="stylesheet" type="text/css" />
      <link href="/classpath/css/layout/default-user/my_layout.css" rel="stylesheet" type="text/css" />
      <!--[if lte IE 7]>
        <link href="/classpath/css/default/patch_my_layout.css" rel="stylesheet" type="text/css" />
      <![endif]-->
  
      <link type="text/css" href="/classpath/css/theme/sunny/jquery-ui-1.8.4.custom.css" rel="stylesheet" />
      <script type="text/javascript" src="/classpath/js/framework/jquery-1.4.2.min.js"></script>
      <script type="text/javascript" src="/classpath/js/framework/jquery-ui-1.8.4.custom.min.js"></script>
      
      <script type="text/javascript" src="/classpath/js/widget/ui.simplewidget.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.titlebar.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.unary.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.binary.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.analog.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.devices.js"></script>
      
      <script type="text/javascript" src="/classpath/js/widget/ui.toolbox.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.widgetcontainer.js"></script>
      <script type="text/javascript" src="/classpath/js/widget/ui.navigator.js"></script>
      <link type="text/css" href="/classpath/css/widget/navigator.css" media="screen" rel="stylesheet" />
  	
      <script type="text/javascript" src="/classpath/plugin/draggable/jquery-collide.js"></script> 
      
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.fancybox-1.3.0.pack.js"></script>
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.easing-1.3.pack.js"></script>
      <script type="text/javascript" src="/classpath/plugin/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
      <link type="text/css" href="/classpath/plugin/fancybox/jquery.fancybox-1.3.0.css" rel="stylesheet" />
	
      <script type="text/javascript" src="/classpath/plugin/wresize/jquery.wresize.js"></script>
      { if (User.loggedIn_? && User.superUser_?){
          <script type="text/javascript" src="/classpath/plugin/sidebar/jquery.sidebar.js"></script>
          <link type="text/css" href="/classpath/plugin/sidebar/dark-glass/sidebar.css" rel="stylesheet" />
          <script type="text/javascript" src="/classpath/plugin/jquery-message/jquery.message.js"></script>
          <link type="text/css" href="/classpath/plugin/jquery-message/jquery.message.css" rel="stylesheet" />
          <script type="text/javascript" src="/classpath/plugin/uniform/jquery.uniform.js"></script>
          <link type="text/css" href="/classpath/plugin/uniform/css/uniform.default.css" rel="stylesheet" />
          <script type="text/javascript">
            $(document.body).ready(function(){
              $("#col2_content").sidebar({
                height: 300,
                pheight: 800,
                width: 400,
                top: 40,
                ptop: 30,
                position: "right",
                injectWidth: 40
              });
              $("#AdminSideBar").sidebar({
                height: 350,
                pheight: 800,
                width: 400,
                top: 360,
                ptop: 30,
                position: "right",
                injectWidth: 40
              });
              $("input:file").uniform();
              $("button, input:submit").addClass("ui-state-default ui-corner-all")
              .hover(
                  function(){ 
                    $(this).addClass("ui-state-hover"); 
                  },
                  function(){ 
                    $(this).removeClass("ui-state-hover"); 
                  }
              )
              .attr("style", "outline: 0;margin:0 4px 0 0;padding: .4em 1em;text-decoration:none !important;" + 
                    "cursor:pointer;position: relative;text-align: center;zoom: 1;");
            });
          </script>
        }
      }
    </head>
	}
}