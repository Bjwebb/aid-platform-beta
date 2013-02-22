/*! HTML5 Shiv vpre3.1 | @jon_neal @afarkas @rem | MIT/GPL2 Licensed */

(function(a,b){var c=function(a){return a.innerHTML="<x-element></x-element>",a.childNodes.length===1}(b.createElement("a")),d=function(a,b,c){return b.appendChild(a),(c=(c?c(a):a.currentStyle).display)&&b.removeChild(a)&&c==="block"}(b.createElement("nav"),b.documentElement,a.getComputedStyle),e="urn:schemas-microsoft-com:vml",f={elements:"abbr article aside audio bdi canvas data datalist details figcaption figure footer header hgroup mark meter nav output progress section summary time video".split(" "),fixDomMethods:!0,shivDom:function(a){if(a.documentShived)return;var b=a.createElement,c=a.createDocumentFragment,d=function(a){b(a)};for(var g=0,h=f.elements.length;g<h;++g)b(f.elements[g]);a.createElement=function(a){var c=b(a);return f.fixDomMethods&&c.canHaveChildren&&c.xmlns!=e&&c.tagUrn!=e&&f.shivDom(c.document),c},a.createDocumentFragment=function(){var a=c();return f.fixDomMethods&&f.shivDom(a),a}},shivDocument:function(a){a=a||b;if(a.documentShived)return;var e=a.getElementsByTagName("head")[0];!c&&f.fixDomMethods&&f.shivDom(a),a.documentShived=!0;if(!d&&e){var g=a.createElement("div");g.innerHTML=["x<style>","article,aside,details,figcaption,figure,footer,header,hgroup,nav,section{display:block}","audio{display:none}","canvas,video{display:inline-block;*display:inline;*zoom:1}","[hidden]{display:none}audio[controls]{display:inline-block;*display:inline;*zoom:1}","mark{background:#FF0;color:#000}","</style>"].join(""),e.insertBefore(g.lastChild,e.firstChild)}return a}};f.shivDocument(b),f.type="default",a.html5=f})(this,document),function(a){function b(a){return a.replace(z,N).replace(A,function(a,b,f){for(var a=f.split(","),f=0,h=a.length;f<h;f++){var i=e(a[f].replace(H,N).replace(I,N))+M,j=[];a[f]=i.replace(B,function(a,b,e,f,h){if(b){if(j.length>0){var a=j,k,h=i.substring(0,h).replace(C,L);if(h==L||h.charAt(h.length-1)==M)h+="*";try{k=q(h)}catch(l){}if(k){h=0;for(e=k.length;h<e;h++){for(var f=k[h],m=f.className,n=0,o=a.length;n<o;n++){var p=a[n];!RegExp("(^|\\s)"+p.className+"(\\s|$)").test(f.className)&&p.b&&(p.b===!0||p.b(f)===!0)&&(m=g(m,p.className,!0))}f.className=m}}j=[]}return b}return(b=e?c(e):!G||G.test(f)?{className:d(f),b:!0}:null)?(j.push(b),"."+b.className):a})}return b+a.join(",")})}function c(b){var c=!0,e=d(b.slice(1)),g=b.substring(0,5)==":not(",i,j;g&&(b=b.slice(5,-1));var k=b.indexOf("(");k>-1&&(b=b.substring(0,k));if(b.charAt(0)==":")switch(b.slice(1)){case"root":c=function(a){return g?a!=m:a==m};break;case"target":if(o==8){c=function(b){function c(){var a=location.hash,c=a.slice(1);return g?a==L||b.id!=c:a!=L&&b.id==c}return h(a,"hashchange",function(){f(b,e,c())}),c()};break}return!1;case"checked":c=function(a){return F.test(a.type)&&h(a,"propertychange",function(){event.propertyName=="checked"&&f(a,e,a.checked!==g)}),a.checked!==g};break;case"disabled":g=!g;case"enabled":c=function(a){return E.test(a.tagName)?(h(a,"propertychange",function(){event.propertyName=="$disabled"&&f(a,e,a.a===g)}),r.push(a),a.a=a.disabled,a.disabled===g):b==":enabled"?g:!g};break;case"focus":i="focus",j="blur";case"hover":i||(i="mouseenter",j="mouseleave"),c=function(a){return h(a,g?j:i,function(){f(a,e,!0)}),h(a,g?i:j,function(){f(a,e,!1)}),g};break;default:if(!y.test(b))return!1}return{className:e,b:c}}function d(a){return u+"-"+(o==6&&t?s++:a.replace(D,function(a){return a.charCodeAt(0)}))}function e(a){return a.replace(K,N).replace(J,M)}function f(a,b,c){var d=a.className,b=g(d,b,c);b!=d&&(a.className=b,a.parentNode.className+=L)}function g(a,b,c){var d=RegExp("(^|\\s)"+b+"(\\s|$)"),e=d.test(a);return c?e?a:a+M+b:e?a.replace(d,N).replace(K,N):a}function h(a,b,c){a.attachEvent("on"+b,c)}function i(a,b){if(/^https?:\/\//i.test(a))return b.substring(0,b.indexOf("/",8))==a.substring(0,a.indexOf("/",8))?a:null;if(a.charAt(0)=="/")return b.substring(0,b.indexOf("/",8))+a;var c=b.split(/[?#]/)[0];return a.charAt(0)!="?"&&c.charAt(c.length-1)!="/"&&(c=c.substring(0,c.lastIndexOf("/")+1)),c+a}function j(a){return a?(n.open("GET",a,!1),n.send(),(n.status==200?n.responseText:L).replace(v,L).replace(w,function(b,c,d,e,f){return j(i(d||f,a))}).replace(x,function(b,c,d){return c=c||L," url("+c+i(d,a)+c+") "})):L}function k(){var a,c;a=l.getElementsByTagName("BASE");for(var d=a.length>0?a[0].href:l.location.href,e=0;e<l.styleSheets.length;e++)if(c=l.styleSheets[e],c.href!=L&&(a=i(c.href,d)))c.cssText=b(j(a));r.length>0&&setInterval(function(){for(var a=0,b=r.length;a<b;a++){var c=r[a];c.disabled!==c.a&&(c.disabled?(c.disabled=!1,c.a=!0,c.disabled=!0):c.a=c.disabled)}},250)}}(this);var JSON;JSON||(JSON={}),function(){function f(a){return a<10?"0"+a:a}function quote(a){return escapable.lastIndex=0,escapable.test(a)?'"'+a.replace(escapable,function(a){var b=meta[a];return typeof b=="string"?b:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function str(a,b){var c,d,e,f,g=gap,h,i=b[a];i&&typeof i=="object"&&typeof i.toJSON=="function"&&(i=i.toJSON(a)),typeof rep=="function"&&(i=rep.call(b,a,i));switch(typeof i){case"string":return quote(i);case"number":return isFinite(i)?String(i):"null";case"boolean":case"null":return String(i);case"object":if(!i)return"null";gap+=indent,h=[];if(Object.prototype.toString.apply(i)==="[object Array]"){f=i.length;for(c=0;c<f;c+=1)h[c]=str(c,i)||"null";return e=h.length===0?"[]":gap?"[\n"+gap+h.join(",\n"+gap)+"\n"+g+"]":"["+h.join(",")+"]",gap=g,e}if(rep&&typeof rep=="object"){f=rep.length;for(c=0;c<f;c+=1)typeof rep[c]=="string"&&(d=rep[c],e=str(d,i),e&&h.push(quote(d)+(gap?": ":":")+e))}else for(d in i)Object.prototype.hasOwnProperty.call(i,d)&&(e=str(d,i),e&&h.push(quote(d)+(gap?": ":":")+e));return e=h.length===0?"{}":gap?"{\n"+gap+h.join(",\n"+gap)+"\n"+g+"}":"{"+h.join(",")+"}",gap=g,e}}"use strict",typeof Date.prototype.toJSON!="function"&&(Date.prototype.toJSON=function(a){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+f(this.getUTCMonth()+1)+"-"+f(this.getUTCDate())+"T"+f(this.getUTCHours())+":"+f(this.getUTCMinutes())+":"+f(this.getUTCSeconds())+"Z":null},String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(a){return this.valueOf()});var cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,escapable=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,gap,indent,meta={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},rep;typeof JSON.stringify!="function"&&(JSON.stringify=function(a,b,c){var d;gap="",indent="";if(typeof c=="number")for(d=0;d<c;d+=1)indent+=" ";else typeof c=="string"&&(indent=c);rep=b;if(!b||typeof b=="function"||typeof b=="object"&&typeof b.length=="number")return str("",{"":a});throw new Error("JSON.stringify")}),typeof JSON.parse!="function"&&(JSON.parse=function(text,reviver){function walk(a,b){var c,d,e=a[b];if(e&&typeof e=="object")for(c in e)Object.prototype.hasOwnProperty.call(e,c)&&(d=walk(e,c),d!==undefined?e[c]=d:delete e[c]);return reviver.call(a,b,e)}var j;text=String(text),cx.lastIndex=0,cx.test(text)&&(text=text.replace(cx,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,"]").replace(/(?:^|:|,)(?:\s*\[)+/g,"")))return j=eval("("+text+")"),typeof reviver=="function"?walk({"":j},""):j;throw new SyntaxError("JSON.parse")})}()
;
// https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Array/map
// Production steps of ECMA-262, Edition 5, 15.4.4.19
// Reference: http://es5.github.com/#x15.4.4.19
if (!Array.prototype.map) {
    Array.prototype.map = function(callback, thisArg) {

        var T, A, k;

        if (this == null) {
            throw new TypeError(" this is null or not defined");
        }

        // 1. Let O be the result of calling ToObject passing the |this| value as the argument.
        var O = Object(this);

        // 2. Let lenValue be the result of calling the Get internal method of O with the argument "length".
        // 3. Let len be ToUint32(lenValue).
        var len = O.length >>> 0;

        // 4. If IsCallable(callback) is false, throw a TypeError exception.
        // See: http://es5.github.com/#x9.11
        if ({}.toString.call(callback) != "[object Function]") {
            throw new TypeError(callback + " is not a function");
        }

        // 5. If thisArg was supplied, let T be thisArg; else let T be undefined.
        if (thisArg) {
            T = thisArg;
        }

        // 6. Let A be a new array created as if by the expression new Array(len) where Array is
        // the standard built-in constructor with that name and len is the value of len.
        A = new Array(len);

        // 7. Let k be 0
        k = 0;

        // 8. Repeat, while k < len
        while(k < len) {

            var kValue, mappedValue;

            // a. Let Pk be ToString(k).
            //   This is implicit for LHS operands of the in operator
            // b. Let kPresent be the result of calling the HasProperty internal method of O with argument Pk.
            //   This step can be combined with c
            // c. If kPresent is true, then
            if (k in O) {

                // i. Let kValue be the result of calling the Get internal method of O with argument Pk.
                kValue = O[ k ];

                // ii. Let mappedValue be the result of calling the Call internal method of callback
                // with T as the this value and argument list containing kValue, k, and O.
                mappedValue = callback.call(T, kValue, k, O);

                // iii. Call the DefineOwnProperty internal method of A with arguments
                // Pk, Property Descriptor {Value: mappedValue, Writable: true, Enumerable: true, Configurable: true},
                // and false.

                // In browsers that support Object.defineProperty, use the following:
                // Object.defineProperty(A, Pk, { value: mappedValue, writable: true, enumerable: true, configurable: true });

                // For best browser support, use the following:
                A[ k ] = mappedValue;
            }
            // d. Increase k by 1.
            k++;
        }

        // 9. return A
        return A;
    };
}

// https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Array/forEach
// Production steps of ECMA-262, Edition 5, 15.4.4.18
// Reference: http://es5.github.com/#x15.4.4.18
if ( !Array.prototype.forEach ) {

    Array.prototype.forEach = function( callback, thisArg ) {

        var T, k;

        if ( this == null ) {
            throw new TypeError( " this is null or not defined" );
        }

        // 1. Let O be the result of calling ToObject passing the |this| value as the argument.
        var O = Object(this);

        // 2. Let lenValue be the result of calling the Get internal method of O with the argument "length".
        // 3. Let len be ToUint32(lenValue).
        var len = O.length >>> 0; // Hack to convert O.length to a UInt32

        // 4. If IsCallable(callback) is false, throw a TypeError exception.
        // See: http://es5.github.com/#x9.11
        if ( {}.toString.call(callback) != "[object Function]" ) {
            throw new TypeError( callback + " is not a function" );
        }

        // 5. If thisArg was supplied, let T be thisArg; else let T be undefined.
        if ( thisArg ) {
            T = thisArg;
        }

        // 6. Let k be 0
        k = 0;

        // 7. Repeat, while k < len
        while( k < len ) {

            var kValue;

            // a. Let Pk be ToString(k).
            //   This is implicit for LHS operands of the in operator
            // b. Let kPresent be the result of calling the HasProperty internal method of O with argument Pk.
            //   This step can be combined with c
            // c. If kPresent is true, then
            if ( k in O ) {

                // i. Let kValue be the result of calling the Get internal method of O with argument Pk.
                kValue = O[ k ];

                // ii. Call the Call internal method of callback with T as the this value and
                // argument list containing kValue, k, and O.
                callback.call( T, kValue, k, O );
            }
            // d. Increase k by 1.
            k++;
        }
        // 8. return undefined
    };
}

// https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Array/indexOf
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (searchElement /*, fromIndex */ ) {
        "use strict";
        if (this == null) {
            throw new TypeError();
        }
        var t = Object(this);
        var len = t.length >>> 0;
        if (len === 0) {
            return -1;
        }
        var n = 0;
        if (arguments.length > 0) {
            n = Number(arguments[1]);
            if (n != n) { // shortcut for verifying if it's NaN
                n = 0;
            } else if (n != 0 && n != Infinity && n != -Infinity) {
                n = (n > 0 || -1) * Math.floor(Math.abs(n));
            }
        }
        if (n >= len) {
            return -1;
        }
        var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
        for (; k < len; k++) {
            if (k in t && t[k] === searchElement) {
                return k;
            }
        }
        return -1;
    }
}

// https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Array/filter
if (!Array.prototype.filter)
{
    Array.prototype.filter = function(fun /*, thisp */)
    {
        "use strict";

        if (this == null)
            throw new TypeError();

        var t = Object(this);
        var len = t.length >>> 0;
        if (typeof fun != "function")
            throw new TypeError();

        var res = [];
        var thisp = arguments[1];
        for (var i = 0; i < len; i++)
        {
            if (i in t)
            {
                var val = t[i]; // in case fun mutates this
                if (fun.call(thisp, val, i, t))
                    res.push(val);
            }
        }

        return res;
    };
}

if (!String.prototype.trim) {
    String.prototype.trim = function() {
      return this.replace(/^\s+|\s+$/g, '');
    };
}


window.CSSStyleDeclaration = window.CSSStyleDeclaration || function() { }

// https://github.com/mbostock/d3/issues/199#issuecomment-1487129
if (!CSSStyleDeclaration.prototype.getProperty)
    CSSStyleDeclaration.prototype.getProperty = function(a) {
        return this.getAttribute(a);
    };

if(!CSSStyleDeclaration.prototype.setProperty)
    CSSStyleDeclaration.prototype.setProperty = function(a,b) {
        return this.setAttribute(a,b);
    };

if(!CSSStyleDeclaration.prototype.removeProperty)
    CSSStyleDeclaration.prototype.removeProperty = function(a) {
        return this.removeAttribute(a);
    };


// https://github.com/mbostock/d3/issues/42#issuecomment-1265410
if(!document.createElementNS)
    document.createElementNS = function(ns, name) {
        return document.createElement(name);
    };

if (!window.JSON) {
    window.JSON = {};
}
if (!window.JSON.parse) {
    window.JSON.parse = (function() {
       var rvalidchars = /^[\],:{}\s]*$/,
           rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g,
           rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,
           rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d\d*\.|)\d+(?:[eE][\-+]?\d+|)/g;
       return function(data) {
            if ( rvalidchars.test( data.replace( rvalidescape, "@" )
                .replace( rvalidtokens, "]" )
                .replace( rvalidbraces, "")) )
            {
                return ( new Function( "return " + data ) )();
            }
            
            return null; // Invalid JSON
        }
    })();
}
;


