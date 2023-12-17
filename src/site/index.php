<!DOCTYPE html>
<html lang="en" dir="ltr">
    <head>
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-19225692-4"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());
            gtag('config', 'UA-19225692-4');
        </script>
        <title>IF97 steam tables</title>
        <meta charset="UTF-8" />
        <meta name='copyright' content='Copyright 2009-2023 by Hummeling Engineering BV' />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <meta http-equiv="Cache-Control" content="no-cache" />
        <link href='https://fonts.googleapis.com/css?family=Abel' rel='stylesheet'>
        <link href='https://fonts.googleapis.com/css?family=Orbitron' rel='stylesheet'>
        <link rel="shortcut icon" href="favicon.ico" />
        <script src="https://cdn.nocodeflow.net/tools/geoblock.js"></script>
        <style>
            html {
                background: linear-gradient(#fff, #eef) no-repeat fixed;
                background-size: cover;
                margin: 0;
                padding: 0;
            }
            header, section, footer, aside, nav, main, article, figure {
                display: block; /* for ancient browsers */
            }
            nav {
                display: inline;
            }
            body {
                background: rgba(255, 255, 255, 0.5) url(img/diagramIF97.png) no-repeat bottom right;
                color: #566;
                font: 20px Abel, Verdana, "sans serif";
                margin: auto;
                max-width: 52em;
            }
            code, pre {
                background: #fff;
                border: 1px solid #F80;
                padding: 0 4pt;
            }
            a {
                color: #357;
                text-decoration: underline;
                position: relative;
                display: inline-block;
                text-indent: 0;
            }
            a:hover {
                color: #f80;
            }
            a.active {
                border: 5px solid #024;
                color: #024;
                margin: 0 5px 0 0;
            }
            a.menu {
                margin: 0 10px 0 0;
                text-decoration: none;
            }
            a.active, a.menu {
                border-radius: 0.5em;
                padding: 0.5em 2em 0.5em 0;
                text-decoration: none;
            }
            a:active {
                color: #f80;
            }
            #logo {
                clear: none;
                display: inline;
                margin: 0;
                padding: 1em 0 0 0;
            }
            main {
                border-radius: 0.5em;
                padding: 0;
            }
            header {
                margin: 0;
            }
            h1, footer {
                background-color: rgb(0, 34, 68);
                background-color: rgba(0, 34, 68, 0.5);
                font-family: Orbitron;
                font-weight: normal;
            }
            section {
                background-color: rgb(255, 255, 255);
                background-color: rgba(255, 255, 255, 0.9);
                overflow: auto;
            }
            article, nav {
                padding: 1em;
            }
            footer {
                color: #fff;
                font-size: 12px;
                padding: .1em;
            }
            #bottom {
                clear: both;
                height: 100%;
            }
            h1 {
                background: #024;
                border-radius: 8px;
                color: #FFF;
                font-weight: normal;
                padding: 1em 1em 0.5em 1em;
                text-shadow: 0 0 8px #0FF, 0 0 16px #0FF;
            }
            h2 {
                border-left: 4px solid #F80;
                margin: 1em;
                padding: 0 1em;
                clear: left;
                font-size: 20px;
            }
            h3 {
                font-size: 18px;
                padding: 0 2em;
            }
            p:first-of-type {
                text-indent: 2em;
            }
            form, p, table {
                padding: 0 2em;
            }
            img {
                border: 0;
                display: inline-block;
                position: relative;
            }
            figure.zoom, img.zoom {
                border: 2px solid #f80;
                border-radius: 0.5em;
                clear: right;
                float: right;
                margin: 0.5em 0 0.5em 1em;
            }
            figure.zoom:hover, img.zoom:hover {
                border: 5px solid #024;
                border-radius: 0.5em;
                width: 100%;
                margin: 0.5em 0 0.5em 0;
                z-index: 100;
            }
            img.background {
                position: fixed;
                right: 0;
                bottom: 0;
                z-index: -1;
            }
            .left {
                clear: none;
                float: left;
                margin: .5em 1em;
            }
            img.inline {
                clear: none;
                float: right;
                margin: .5em 1em;
            }
            span.smallcaps {
                font-size: 15px;
            }
            ul {
                padding-left: 2em;
            }
            li {
                padding-bottom: 0.5em;
            }
            td {
                text-align: center;
            }
            .right {
                text-align: right;
            }
            hr {
                clear: both;
            }
        </style>
    </head>
    <body>
        <h1 id="IF97">Java IF97</h1>
        <img src="img/IF97.svg" onerror="this.src='img/IF97.102.png'" style="float:right; padding:0 1em;" width="102" height="102" alt="Java IF97 library" />
        <p>IAPWS-IF97 is the Industrial Formulation by The International Association for the Properties of Water and Steam (<a href="http://www.iapws.org/">www.iapws.org</a>). This implementation is according Wagner, Wolfgang &amp; Kretzschmar, Hans-Joachim, 2008, <i>International Steam Tables &mdash; Properties of Water and Steam Based on the Industrial Formulation IAPWS-IF97</i>, 2<sup>nd</sup> Edition, Springer-Verlag, Berlin Heidelberg, ISBN&nbsp;978-3-540-21419-9.</p>
        <p>This library is made available to the public domain, under <a href="http://www.gnu.org/licenses/lgpl-3.0-standalone.html">GNU Lesser General Public License</a>.
            That is, free for any product to use and redistribute. This library can also be included in proprietary software.</p>
        <?php
        $msg = "Stay informed via the IF97 mailing list &rarr;";

        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            $name = $_REQUEST['name'];
            $address = $_REQUEST['address'];

            unset($_REQUEST['name'], $_REQUEST['address']);

            if (filter_var($address, FILTER_VALIDATE_EMAIL)) {
                $mailBox = "engineering@hummeling.com";
                $subject = "IF97 mailing list";
                $user = $name . " <" . $address . ">";
                $header = "From: " . $mailBox . "\r\n";

                if (mail("if97@hummeling.com", $subject, $user, $header)) {
                    $msg = $user . " added to IF97 mailing list.";
                    $message = "Dear " . $name . ",\n" .
                            "\n" .
                            "Thank you for your interest in IF97 Java library!\n" .
                            "You've submitted your email address to the mailing list.\n" .
                            "Send us a message when you want to be removed.\n" .
                            "\n" .
                            "Kind regards,\n" .
                            "\n" .
                            "Ralph Hummeling\n" .
                            "Hummeling Engineering BV\n" .
                            "www.hummeling.com";

                    if (!mail($user, $subject, $message, $header)) {
                        $msg = "Failure sending confirmation.";
                    }
                } else {
                    $msg = "Failure adding " . $user . " to mailing list.";
                }
                $msg = htmlentities($msg);
            } else {
                $msg = "You might have submitted an invalid email address.";
            }
        }
        ?>
        <form action="IF97" method="post"> <?= $msg ?> Name: <input type="text" name="name" /> Email: <input type="email" name="address" /> <input type="submit" value="Add Me" /></form>
        <a href="https://sourceforge.net/projects/if97/files/latest/download" rel="nofollow"><img alt="Download IF97" class="left" src="https://a.fsdn.com/con/app/sf-download-button"></a>
        <a href="https://search.maven.org/search?q=a:%22if97%22"><img alt="Latest Maven IF97 artifact" src="https://img.shields.io/maven-central/v/com.hummeling/if97.svg?label=Latest%20Maven%20IF97%20artifact" /></a>
        <form class="left" action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
            <input type="hidden" name="cmd" value="_s-xclick">
            <input type="hidden" name="hosted_button_id" value="V7Q75F3B3VFJ8">
            <input type="image" src="https://www.paypalobjects.com/en_US/NL/i/btn/btn_donateCC_LG.gif" style="border:none" name="submit" alt="PayPal - The safer, easier way to pay online!">
            <img alt="" border="0" src="https://www.paypalobjects.com/nl_NL/i/scr/pixel.gif" width="1" height="1">
        </form>
        <p>IF97 project binaries and source code are hosted on SourceForge: <a href="https://sourceforge.net/projects/if97/">sourceforge.net/projects/if97</a> as well as compiled sources and API documentation (javadoc).</p>
        <p>Our IF97 library is developed in Java, with a test suite containing a mere 100&nbsp;tests readily available in the source distribution.</p>
        <p>IF97 artefacts are also available in the <a href="https://search.maven.org/search?q=a:if97">Maven Central Repository</a> and can be added to your Maven project dependencies section:</p>
        <pre style="clear:both; background-color:#fff; float:none; margin:auto; margin:1em;">&lt;dependency&gt;
    &lt;groupId&gt;com.hummeling&lt;/groupId&gt;
    &lt;artifactId&gt;if97&lt;/artifactId&gt;
    &lt;version&gt;2.1.0&lt;/version&gt;
&lt;/dependency&gt;</pre>
        <p>This library has no dependencies on other software libraries, except for the test classes which require <a href="http://www.junit.org/">JUnit</a>.</p>
        <h2 id="code-calc">Calculate online</h2>
        <p>See IF97 in action on our engineering portal: <a href="https://www.code-calc.com/calculations/if97">www.code-calc.com</a></p>

        <h2 id="swing">Calculate on your desktop</h2>
        <p>We're preparing a desktop application which can be used in Windows, Mac, and Linux. Add yourself to the mailing list or send us a message when you want to be informed when it's available!</p>

        <h2 id="android">Calculate on your mobile device</h2>
        <p>We're also preparing an Android app. Add yourself to the mailing list or send us a message when you want to be informed when it's available!</p>

        <h1 id="properties">Available Properties</h1>
        <p>Below table shows which properties are available as a function of the two arguments shown in brackets above the columns. Refractive index requires the wavelength of light as an additional third argument. Some properties only require one argument and are denoted as <code>&check;sat.</code>, e.g. saturated pressure as a function of temperature only.</p>
        <table>
            <tr><th></th><th></th><th>(<i>p</i>, <i>T</i>)</th><th>(<i>p</i>, <i>h</i>)</th><th>(<i>p</i>, <i>s</i>)</th><th>(<i>h</i>, <i>s</i>)</th><th>(<i>T</i>, <i>s</i>)</th><th>(<i>&rho;</i>, <i>T</i>)</th><th>(<i>p</i>, <i>x</i>?)</th><th>(<i>T</i>, <i>x</i>?)</th></tr>
            <tr><td class="right">absolute pressure</td><td><i>p</i></td><td /><td /><td /><td>&check;</td><td /><td /><td /><td>&check;sat.</td></tr>
            <tr><td class="right">density</td><td><i>&rho;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td class="right">dielectric constant</td><td><i>&epsilon;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td class="right">dynamic viscosity</td><td><i>&eta;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td class="right">isobaric cubic expansion coefficient</td><td><i>&alpha;<sub>v</sub></i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">isothermal compressibility</td><td><i>&kappa;<sub>T</sub></i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">kinematic viscosity</td><td><i>&nu;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td class="right">Prandtl number</td><td><i>Pr</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">refractive index</td><td><i>n</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td class="right">specific enthalpy</td><td><i>h</i></td><td>&check;</td><td /><td>&check;</td><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td class="right">specific entropy</td><td><i>s</i></td><td>&check;</td><td>&check;</td><td /><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td class="right">specific Gibbs free energy</td><td><i>g</i></td><td>&check;</td><td /><td /><td /><td /><td /><td /></tr>
            <tr><td class="right">specific internal energy</td><td><i>u</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">specific isobaric heat capacity</td><td><i>c<sub>p</sub></i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">specific isochoric heat capacity</td><td><i>c<sub>v</sub></i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">specific volume</td><td><i>v</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td class="right">speed of sound</td><td><i>w</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">surface tension</td><td><i>&sigma;</i></td><td /><td /><td /><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td class="right">temperature</td><td><i>T</i></td><td /><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;sat.</td><td /></tr>
            <tr><td class="right">thermal conductivity</td><td><i>&lambda;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td class="right">thermal diffusivity</td><td><i>&kappa;</i></td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td class="right">vapour fraction</td><td><i>x</i></td><td /><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /></tr>
            <tr><td class="right">partial derivative</td><td>(<sup>&part;<i>z</i></sup>/<sub>&part;<i>x</i></sub>)<sub><i>y</i></sub></td><td>&check;</td><td /><td /><td /><td /><td>&check;</td><td /><td /></tr>
        </table>
        <p>Partial derivatives can be specified using the <code>com.hummeling.if97.IF97.Quantity</code> enumeration. For example, the partial derivative of pressure with respect to enthalpy for constant temperature is retrieved as follows:</p>
        <code>double dp_dh = if97.partialDerivativePT(3, 300, IF97.Quantity.h, IF97.Quantity.T, IF97.Quantity.p);</code>
        <p>Note that partial derivatives are returned in the DEFAULT unit system, so (<sup>&part;<i>p</i></sup>/<sub>&part;<i>h</i></sub>)<sub><i>T</i></sub> would be in MPa&middot;kg/kJ.</p>

        <!--<img style="float:right; padding:1em;" src="img/JavaPowered.png" width="76" height="142" alt="Java is a registered trademark of Oracle Corporation." />-->
        <h1 id="usageJava">Using IF97 in Java</h1>
        <a href="https://www.oracle.com/java/" style="float:right; padding:50px;"><img _fcksavedurl="https://www.oracle.com/java/"><img _fcksavedurl="https://www.oracle.com/java/"><img src="img/i-code-java-100x117-1705302.png" alt="I Code Java" border="0" width="100" height="117" /></a>
        <ol>
            <li>Add IF97 to the imports: <code>import com.hummeling.if97.IF97;</code></li>
            <li>Create an IF97 object: <code>IF97 if97 = new IF97(IF97.UnitSystem.ENGINEERING);</code></li>
            <li>Retrieve a property: <code>double v = if97.specificVolumePT(3, 300);</code></li>
        </ol>
        <p>When arguments are out-of-range, an <code>com.hummeling.if97.OutOfRangeException</code> is thrown, which states the exceeded limit. This class contains methods that return the limit, <code>double&nbsp;getLimit()</code>, the quantity, <code>String&nbsp;getQuantity()</code>, and the exceeding value, <code>double&nbsp;getValue()</code>.</p>

        <h2 id="unitSystems">Unit systems</h2>
        <p>Use <code>IF97.UnitSystem</code> enumeration to select a unit system:</p>
        <ul>
            <li><code>DEFAULT</code> Units as used in above mentioned reference Wagner&amp;Kretzschmar.</li>
            <li><code>ENGINEERING</code> Units as commonly used in (process) engineering practice.</li>
            <li><code>SI</code> International system of units.</li>
            <li><code>IMPERIAL</code> British Imperial system of units.</li>
        </ul>
        <table>
            <tr><th></th><th></th><th>DEFAULT</th><th>ENGINEERING</th><th>SI</th><th>IMPERIAL</th></tr>
            <tr><td class="right">absolute pressure</td><td><i>p</i></td><td>MPa</td><td>bar</td><td>Pa</td><td>psi</td></tr>
            <tr><td class="right">density</td><td><i>&rho;</i></td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>lb/ft&sup3;</td></tr>
            <tr><td class="right">dielectric constant</td><td><i>&epsilon;</i></td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td class="right">dynamic viscosity</td><td><i>&eta;</i></td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>cP</td></tr>
            <tr><td class="right">isobaric cubic expansion coefficient</td><td><i>&alpha;<sub>v</sub></i></td><td>1/K</td><td>1/K</td><td>1/K</td><td>1/R</td></tr>
            <tr><td class="right">isothermal compressibility</td><td><i>&kappa;<sub>T</sub></i></td><td>1/MPa</td><td>1/MPa</td><td>1/Pa</td><td>in&sup2;/lb</td></tr>
            <tr><td class="right">kinematic viscosity</td><td><i>&nu;</i></td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td></tr>
            <tr><td class="right">Prandtl number</td><td><i>Pr</i></td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td class="right">refractive index</td><td><i>n</i></td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td class="right">specific enthalpy</td><td><i>h</i></td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td class="right">specific entropy</td><td><i>s</i></td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td class="right">specific Gibbs free energy</td><td><i>g</i></td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td class="right">specific internal energy</td><td><i>u</i></td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td class="right">specific isobaric heat capacity</td><td><i>c<sub>p</sub></i></td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td class="right">specific isochoric heat capacity</td><td><i>c<sub>v</sub></i></td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td class="right">specific volume</td><td><i>v</i></td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>ft&sup3;/lb</td></tr>
            <tr><td class="right">speed of sound</td><td><i>w</i></td><td>m/s</td><td>m/s</td><td>m/s</td><td>ft/s</td></tr>
            <tr><td class="right">surface tension</td><td><i>&sigma;</i></td><td>N/m</td><td>N/m</td><td>N/m</td><td>lbf/ft</td></tr>
            <tr><td class="right">temperature</td><td><i>T</i></td><td>K</td><td>&deg;C</td><td>K</td><td>&deg;F</td></tr>
            <tr><td class="right">thermal conductivity</td><td><i>&lambda;</i></td><td>W/(m&middot;K)</td><td>kW/(m&middot;K)</td><td>W/(m&middot;K)</td><td>BTU/(hr&middot;ft&middot;R)</td></tr>
            <tr><td class="right">thermal diffusivity</td><td><i>&kappa;</i></td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td></tr>
            <tr><td class="right">vapour fraction</td><td><i>x</i></td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td class="right">wavelength of light</td><td><i>&lambda;<sub>L</sub></i></td><td>&mu;m</td><td>&mu;m</td><td>m</td><td>in</td></tr>
        </table>
        <p>Note that partial derivatives are returned in the DEFAULT unit system, so (<sup>&part;<i>p</i></sup>/<sub>&part;<i>h</i></sub>)<sub><i>T</i></sub> would be in MPa&middot;kg/kJ.</p>

        <a href="https://www.mathworks.com/" style="float:right; padding:1em;"><img src="img/MATLAB.png" width="67" height="60" alt="MATLAB is a registered trademark of The MathWorks, inc." /></a>
        <h1 id="usageMATLAB">Accessing IF97 from MATLAB</h1>
        <p>Our IF97 library can be accessed easily from within MATLAB since Java is standard shipped with MATLAB. First add <code>IF97.jar</code> to MATLAB's dynamic class path<br />&gt;&gt; <code>javaaddpath path_to_IF97.jar</code></p>
        <p>Select your preferred unit system<br />&gt;&gt; <code>unitSystem = javaMethod('valueOf', 'com.hummeling.if97.IF97$UnitSystem', 'ENGINEERING');</code></p>
        <p>Create an instance of the IF97 class<br />&gt;&gt; <code>if97 = com.hummeling.if97.IF97(unitSystem);</code></p>
        <p>Retrieve a property, e.g., specific volume as a function of pressure and temperature as follows<br />&gt;&gt; <code>if97.specificVolumePT(3, 300)</code></p>
        <p>Alternatively, the Java archive can be added to MATLAB's static class path by editing your <code>classpath.txt</code> file, see your documentation for more details. Or you could add the <code>javaaddpath</code> statement to your MATLAB startup file, <code>startup.m</code>.</p>
        <p>For a list of available methods issue the following command<br />&gt;&gt; <code>methodsview(if97)</code></p>

        <h1 id="contact">Contact Us</h1>
        <a href="https://www.hummeling.com" style="float:right; padding:0 1em;"><img src="img/HummelingEngineering2.svg" onerror="this.src='img/HummelingEngineering.png'" alt="Hummeling Engineering BV" width="300" height="102" /></a>
        <p>For more information, contact us: <script type="text/javascript">
    var email = "engineering@hummeling.com";
    document.write("<a href='mailto:" + email + "'>" + email + "</a>");
            </script>.
        </p>
        <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
            <input type="hidden" name="cmd" value="_s-xclick">
            <input type="hidden" name="hosted_button_id" value="V7Q75F3B3VFJ8">
            <input type="image" src="https://www.paypalobjects.com/en_US/NL/i/btn/btn_donateCC_LG.gif" style="border:none" name="submit" alt="PayPal - The safer, easier way to pay online!">
            <img alt="" border="0" src="https://www.paypalobjects.com/nl_NL/i/scr/pixel.gif" width="1" height="1">
        </form>
        <p>Happy with our library? Consider a donation!</p>
        <p>We've also implemented the more accurate (but slower) formulation for scientific and general use, IAPWS-95. Contact us for more information.</p>

        <footer><p>&copy; 2009-<script>document.write(new Date().getFullYear());</script> Hummeling Engineering BV all rights reserved &mdash; Trade Register of The Hague 57591113</p></footer>
        <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
        <ins class="adsbygoogle" style="display:block" data-ad-client="ca-pub-5295644918763644" data-ad-slot="5804833411" data-ad-format="auto"></ins>
        <script>(adsbygoogle = window.adsbygoogle || []).push({});</script>
        <script type="text/javascript">
            var Tawk_API = Tawk_API || {}, Tawk_LoadStart = new Date();
            (function () {
                var s1 = document.createElement("script"), s0 = document.getElementsByTagName("script")[0];
                s1.async = true;
                s1.src = 'https://embed.tawk.to/5bf45e9b79ed6453ccaa4f84/default';
                s1.charset = 'UTF-8';
                s1.setAttribute('crossorigin', '*');
                s0.parentNode.insertBefore(s1, s0);
            })();
        </script>
    </body>
</html>
