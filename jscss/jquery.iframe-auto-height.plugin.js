(function ($) {
  $.fn.iframeAutoHeight = function (spec) {
    //$iframeBody[0].scrollHeight = 0;

    var undef;
    if ($.browser == undef) {
      message = [];
      message.push("WARNING: you appear to be using a newer version of jquery which does not support the $.browser variable.");
      message.push("The jQuery iframe auto height plugin relies heavly on the $.browser features.");
      message.push("Install jquery-browser: https://raw.github.com/jquery/jquery-browser/master/src/jquery.browser.js");
      alert(message.join("\n"));
      return $;
    }

    // set default option values
    var options = $.extend({
        heightOffset: 0,
        minHeight: 0,
        callback: function (newHeight) {},
        animate: false,
        debug: false,
        diagnostics: false, // used for development only
        resetToMinHeight: false,
        triggerFunctions: [],
        heightCalculationOverrides: []
      }, spec);

    // logging
    function debug(message) {
      if (options.debug && options.debug === true && window.console) {
        console.log(message);
      }
    }

    // not used by production code
    function showDiagnostics(iframe, calledFrom) {
      debug("Diagnostics from '" + calledFrom + "'");
      try {
        debug("  " + $(iframe, window.top.document).contents().find('body')[0].scrollHeight + " for ...find('body')[0].scrollHeight");
        debug("  " + $(iframe.contentWindow.document).height() + " for ...contentWindow.document).height()");
        debug("  " + $(iframe.contentWindow.document.body).height() + " for ...contentWindow.document.body).height()");
      } catch (ex) {
        // ie fails when called during for each, ok later on
        // probably not an issue if called in a document ready block
        debug("  unable to check in this state");
      }
      debug("End diagnostics -> results vary by browser and when diagnostics are requested");
    }

    // show all option values
    debug(options);

    // ******************************************************
    // iterate over the matched elements passed to the plugin ; return will make it chainable
    return this.each(function () {

      // ******************************************************
      // http://api.jquery.com/jQuery.browser/
      var strategyKeys = ['webkit', 'mozilla', 'msie', 'opera'];
      var strategies = [];
      strategies['default'] = function (iframe, $iframeBody, options, browser) {
        // NOTE: this is how the plugin determines the iframe height, override if you need custom
//alert($iframeBody[0].offsetHeight);
        return $iframeBody[0].scrollHeight + options.heightOffset;
//strategy = $iframeBody[0].scrollHeight + options.heightOffset;
      
      };
//alert("after");
      jQuery.each(strategyKeys, function (index, value) {
        // use the default strategy for all browsers, can be overridden if desired
        strategies[value] = strategies['default'];
      });

      // override strategies if registered in options
      jQuery.each(options.heightCalculationOverrides, function(index, value) {
        strategies[value.browser] = value.calculation;
      });

      function findStrategy(browser) {
        var strategy = null;

        jQuery.each(strategyKeys, function (index, value) {
          if (browser[value]) {
            strategy = strategies[value];
            return false;
          }
        });

        if (strategy === null) {
          strategy = strategies['default'];
        }

        return strategy;
      }
      // ******************************************************

      // for use by webkit only
      var loadCounter = 0;

      // resizeHeight
      function resizeHeight(iframe) {
        if (options.diagnostics) {
          showDiagnostics(iframe, "resizeHeight");
        }
//added by goto
iframe.style.height = 0 + 'px';
        // set the iframe size to minHeight so it'll get smaller on resizes in FF and IE
        if (options.resetToMinHeight && options.resetToMinHeight === true) {
          iframe.style.height = options.minHeight + 'px';
        }
//        else        iframe.style.height = 0+'px';

iframe.style.height = 0 + 'px';
//alert(iframe.style.height);
        // get the iframe body height and set inline style to that plus a little
        var $body = null;
        $body = $(iframe, window.top.document).contents().find('body');
//alert(window.top.document);
        var strategy = findStrategy($.browser);
iframe.style.height = 0 + 'px';
        var newHeight = strategy(iframe, $body, options, $.browser);
//var newHeight = strategy(iframe, $body, 0, $.browser);
//var newHeight = 30;
//
//alert(newHeight);
        debug(newHeight);

        if (newHeight < options.minHeight) {
          debug("new height is less than minHeight");
          newHeight = options.minHeight + options.heightOffset;
        }

        debug("New Height: " + newHeight);
        if (options.animate) {
          $(iframe).animate({height: newHeight + 'px'}, {duration: 500});
        } else {
          iframe.style.height = newHeight + 'px';
        }

        options.callback.apply($(iframe), [{newFrameHeight: newHeight}]);
      } // END resizeHeight

      // debug me
      debug(this);
      if (options.diagnostics) {
        showDiagnostics(this, "each iframe");
      }

      // if trigger functions are registered, invoke them
      if (options.triggerFunctions.length > 0) {
        debug(options.triggerFunctions.length + " trigger Functions");
        for (var i = 0; i < options.triggerFunctions.length; i++) {
          options.triggerFunctions[i](resizeHeight, this);
        }
      }

      // Check if browser is Webkit (Safari/Chrome) or Opera
      if ($.browser.webkit || $.browser.opera) {
        debug("browser is webkit or opera");

        // Start timer when loaded.
        $(this).load(function () {
          var delay = 0;
          var iframe = this;
//alert($(this).attr('src'));

iframe.style.height = 0+'px';
          var delayedResize = function () {
//alert("webkit");
iframe.style.height = 0+'px';
            resizeHeight(iframe);
          };
iframe.style.height = 0+'px';
          if (loadCounter === 0) {
            // delay the first one
            delay = 500;
          } else {
            // Reset iframe height to 0 to force new frame size to fit window properly
            // this is only an issue when going from large to small iframe, not executed on page load
            iframe.style.height = options.minHeight + 'px';
          }
iframe.style.height = 0+'px';

          debug("load delay: " + delay);
          setTimeout(delayedResize, delay);
          loadCounter++;
        });
//alert($(this).attr('src'));
        // Safari and Opera need a kick-start.
        var source = $(this).attr('src');
        $(this).attr('src', '');
        $(this).attr('src', source);
      } else {
//this.style.height = 0+'px';      
//alert("else");
        // For other browsers.
        $(this).load(function () {
          resizeHeight(this);
        });
      } // if browser

    }); // $(this).each(function () {
  }; // $.fn.iframeAutoHeight = function (options) {
}(jQuery)); // (function ($) {
