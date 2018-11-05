<!DOCTYPE html>
<html lang="en" dir="ltr">
    <head>
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-19225692-4" />
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
        <meta name='copyright' content='Copyright 2009-2018 by Hummeling Engineering BV' />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <meta http-equiv="Cache-Control" content="no-cache" />
        <link rel="shortcut icon" href="favicon.ico" />
        <style>
            html {
                background: linear-gradient(#fff, #eef) no-repeat fixed;
                background-size: cover;
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
                font: 16px Verdana, "sans serif";
                margin: auto;
                max-width: 66em;
                padding: 2em;
            }
            code {
                background: #fff;
                border: 2px solid #fff;
                color: #000;
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
                color: #f80;
                margin: 0;
                padding: 1em;
                text-align: center;
            }
            h2 {
                clear: left;
                font-size: 20px;
            }
            h3 {
                font-size: 18px;
            }
            p:first-of-type {
                text-indent: 2em;
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
            img.inline{
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
            hr {
                clear: both;
            }
        </style>
    </head>
    <body>
        <img class="inline" src="img/IF97.svg" onerror="this.src='img/IF97.102.png'" width="102" height="102" alt="Java IF97 library" />
        <p>IAPWS-IF97 is the Industrial Formulation by The International Association for the Properties of Water and Steam (<a href="http://www.iapws.org/">www.iapws.org</a>).</p>
        <p>Hummeling Engineering released its water &amp; steam thermodynamic properties library to the public domain, under <a href="http://www.gnu.org/licenses/lgpl-3.0-standalone.html">GNU Lesser General Public License</a>. That is, free for any product to use and redistribute. Even proprietary software developers can use our library.</p>
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
        <form class="left" action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
            <input type="hidden" name="cmd" value="_s-xclick">
            <input type="hidden" name="hosted_button_id" value="V7Q75F3B3VFJ8">
            <input type="image" src="https://www.paypalobjects.com/en_US/NL/i/btn/btn_donateCC_LG.gif" style="border:none" name="submit" alt="PayPal - The safer, easier way to pay online!">
            <img alt="" border="0" src="https://www.paypalobjects.com/nl_NL/i/scr/pixel.gif" width="1" height="1">
        </form>
        <p>IF97 project binaries and source code are hosted on SourceForge: <a href="https://sourceforge.net/projects/if97/">sourceforge.net/projects/if97</a> as well as compiled sources and API documentation (javadoc).</p>
        <p>Our IF97 library is developed in Java, with a test suite containing a mere 100&nbsp;tests readily available in the source distribution.</p>
        <p>IF97 artefacts are also available in <a href="http://search.maven.org">The Central Repository</a> and can be added to your Maven project dependencies section:</p>
        <pre style="background-color:#fff;color:#000;">&lt;dependency&gt;
    &lt;groupId&gt;com.hummeling&lt;/groupId&gt;
    &lt;artifactId&gt;if97&lt;/artifactId&gt;
    &lt;version&gt;1.0.5&lt;/version&gt;
&lt;/dependency&gt;</pre>
        <p>This library has no dependencies on other software libraries, except for the test classes which require <a href="http://www.junit.org/">JUnit</a>.</p>
        <h2>&gt;&gt; Available properties</h2>
        <table>
            <tr><th></th><th></th><th>(<i>p</i>, <i>T</i>)</th><th>(<i>p</i>, <i>h</i>)</th><th>(<i>p</i>, <i>s</i>)</th><th>(<i>h</i>, <i>s</i>)</th><th>(<i>T</i>, <i>s</i>)</th><th>(<i>&rho;</i>, <i>T</i>)</th><th>(<i>p</i>, <i>x</i>?)</th><th>(<i>T</i>, <i>x</i>?)</th></tr>
            <tr><td><i>&rho;</i></td><td>density</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td><i>&epsilon;</i></td><td>dielectric constant</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td><i>&eta;</i></td><td>dynamic viscosity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td><i>&alpha;<sub>v</sub></i></td><td>isobaric cubic expansion coefficient</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>&kappa;<sub>T</sub></i></td><td>isothermal compressibility</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>&nu;</i></td><td>kinematic viscosity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td><i>Pr</i></td><td>Prandtl number</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>p</i></td><td>absolute pressure</td><td /><td /><td /><td>&check;</td><td /><td /><td /><td>&check;sat.</td></tr>
            <tr><td><i>n</i></td><td>refractive index</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td><i>h</i></td><td>specific enthalpy</td><td>&check;</td><td /><td>&check;</td><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td><i>u</i></td><td>specific internal energy</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>s</i></td><td>specific entropy</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td><i>c<sub>p</sub></i></td><td>specific isobaric heat capacity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>c<sub>v</sub></i></td><td>specific isochoric heat capacity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>v</i></td><td>specific volume</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td><i>w</i></td><td>speed of sound</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>&sigma;</i></td><td>surface tension</td><td /><td /><td /><td /><td /><td /><td>&check;</td><td>&check;</td></tr>
            <tr><td><i>T</i></td><td>temperature</td><td /><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td>&check;sat.</td><td /></tr>
            <tr><td><i>&lambda;</i></td><td>thermal conductivity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td>&check;</td><td /><td /></tr>
            <tr><td><i>&kappa;</i></td><td>thermal diffusivity</td><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /><td /></tr>
            <tr><td><i>x</i></td><td>vapour fraction</td><td /><td>&check;</td><td>&check;</td><td>&check;</td><td>&check;</td><td /><td /><td /></tr>
            <tr><td><i>g</i></td><td>specific Gibbs free energy</td><td>&check;</td><td /><td /><td /><td /><td /><td /></tr>
            <tr><td>(<sup>&part;<i>z</i></sup>/<sub>&part;<i>x</i></sub>)<sub><i>y</i></sub></td><td>partial derivative</td><td>&check;</td><td /><td /><td /><td /><td>&check;</td><td /><td /></tr>
        </table>
        <p>Partial derivatives can be specified using the <code>com.hummeling.if97.IF97.Quantity</code> enumeration. For example, the partial derivative of pressure with respect to enthalpy for constant temperature is retrieved as follows:</p>
        <code>double dp_dh = if97.partialDerivativePT(3, 300, IF97.Quantity.h, IF97.Quantity.T, IF97.Quantity.p);</code>
        <p>Note that partial derivatives are returned in the DEFAULT unit system, so (<sup>&part;<i>p</i></sup>/<sub>&part;<i>h</i></sub>)<sub><i>T</i></sub> would be in MPa&middot;kg/kJ.</p>
        <h2>&gt;&gt; Using IF97 in Java</h2>
        <ol>
            <li>Add IF97 to the imports: <code>import com.hummeling.if97.IF97;</code></li>
            <li>Create an IF97 object: <code>IF97 if97 = new IF97(IF97.UnitSystem.ENGINEERING);</code></li>
            <li>Retrieve a property: <code>double v = if97.specificVolumePT(3, 300);</code></li>
        </ol>
        <p>When arguments are out-of-range, an <code>com.hummeling.if97.OutOfRangeException</code> is thrown, which states the exceeded limit. This class contains methods that return the limit (<code>double getLimit()</code>), the quantity (<code>String getQuantity()</code>), and the exceeding value (<code>double getValue()</code>).</p>
        <p>Use <code>IF97.UnitSystem</code> enumeration to select a unit system:</p>
        <ul>
            <li><code>DEFAULT</code></li>
            <li><code>ENGINEERING</code></li>
            <li><code>SI</code></li>
            <li><code>IMPERIAL</code></li>
        </ul>
        <table>
            <tr><th></th><th></th><th>DEFAULT</th><th>ENGINEERING</th><th>SI</th><th>IMPERIAL</th></tr>
            <tr><td><i>p</i></td><td>absolute pressure</td><td>MPa</td><td>bar</td><td>Pa</td><td>psi</td></tr>
            <tr><td><i>&rho;</i></td><td>density</td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>kg/m&sup3;</td><td>lb/ft&sup3;</td></tr>
            <tr><td><i>&epsilon;</i></td><td>dielectric constant</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td><i>&eta;</i></td><td>dynamic viscosity</td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>Pa&middot;s</td><td>cP</td></tr>
            <tr><td><i>&alpha;<sub>v</sub></i></td><td>isobaric cubic expansion coefficient</td><td>1/K</td><td>1/K</td><td>1/K</td><td>1/R</td></tr>
            <tr><td><i>&kappa;<sub>T</sub></i></td><td>isothermal compressibility</td><td>1/MPa</td><td>1/MPa</td><td>1/Pa</td><td>in&sup2;/lb</td></tr>
            <tr><td><i>&nu;</i></td><td>kinematic viscosity</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td></tr>
            <tr><td><i>&kappa;</i></td><td>thermal diffusivity</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>m&sup2;/s</td><td>cSt</td></tr>
            <tr><td><i>Pr</i></td><td>Prandtl number</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td><i>n</i></td><td>refractive index</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td><i>h</i></td><td>specific enthalpy</td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td><i>u</i></td><td>specific internal energy</td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td><i>g</i></td><td>specific Gibbs free energy</td><td>kJ/kg</td><td>kJ/kg</td><td>J/kg</td><td>BTU/lb</td></tr>
            <tr><td><i>s</i></td><td>specific entropy</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td><i>c<sub>p</sub></i></td><td>specific isobaric heat capacity</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td><i>c<sub>v</sub></i></td><td>specific isochoric heat capacity</td><td>kJ/(kg&middot;K)</td><td>kJ/(kg&middot;K)</td><td>J/(kg&middot;K)</td><td>BTU/(lb&middot;R)</td></tr>
            <tr><td><i>v</i></td><td>specific volume</td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>m&sup3;/kg</td><td>ft&sup3;/lb</td></tr>
            <tr><td><i>w</i></td><td>speed of sound</td><td>m/s</td><td>m/s</td><td>m/s</td><td>ft/s</td></tr>
            <tr><td><i>&sigma;</i></td><td>surface tension</td><td>N/m</td><td>N/m</td><td>N/m</td><td>lbf/ft</td></tr>
            <tr><td><i>T</i></td><td>temperature</td><td>K</td><td>&deg;C</td><td>K</td><td>&deg;F</td></tr>
            <tr><td><i>&lambda;</i></td><td>thermal conductivity</td><td>W/(m&middot;K)</td><td>kW/(m&middot;K)</td><td>W/(m&middot;K)</td><td>BTU/(hr&middot;ft&middot;R)</td></tr>
            <tr><td><i>x</i></td><td>vapour fraction</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>
            <tr><td><i>&lambda;<sub>L</sub></i></td><td>wavelength of light</td><td>&mu;m</td><td>&mu;m</td><td>m</td><td>in</td></tr>
        </table>
        <p>Note that partial derivatives are returned in the DEFAULT unit system.</p>
        <h2>&gt;&gt; Accessing IF97 from MATLAB</h2>
        <img class="left" src="img/MATLAB.png" width="50" height="45" alt="MATLAB is a registered trademark of The MathWorks, inc." />
        <p>Our IF97 library can be accessed easily from within MATLAB since Java is standard shipped with MATLAB. First add <code>IF97.jar</code> to MATLAB's dynamic class path<br />&gt;&gt; <code>javaaddpath path_to_IF97.jar</code></p>
        <p>Select your preferred unit system<br />&gt;&gt; <code>unitSystem = javaMethod('valueOf', 'com.hummeling.if97.IF97$UnitSystem', 'ENGINEERING');</code></p>
        <p>Create an instance of the IF97 class<br />&gt;&gt; <code>if97 = com.hummeling.if97.IF97(unitSystem);</code></p>
        <p>Retrieve a property, e.g., specific volume as a function of pressure and temperature as follows<br />&gt;&gt; <code>if97.specificVolumePT(3, 300)</code></p>
        <p>Alternatively, the Java archive can be added to MATLAB's static class path by editing your <code>classpath.txt</code> file, see your documentation for more details. Or you could add the <code>javaaddpath</code> statement to your MATLAB startup file, <code>startup.m</code>.</p>
        <p>For a list of available methods issue the following command<br />&gt;&gt; <code>methodsview(if97)</code></p>

        <h3>&gt;&gt; A project by</h3>
        <a href="https://www.hummeling.com"><img src="img/HummelingEngineering2.svg" onerror="this.src='img/HummelingEngineering.png'" alt="Hummeling Engineering BV" width="300" height="102" /></a>
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
        <p>For an implementation in C, refer to <a href="http://sourceforge.net/projects/freesteam">sourceforge.net/projects/freesteam</a>.</p>
        <script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
        <ins class="adsbygoogle" style="display:block" data-ad-client="ca-pub-5295644918763644" data-ad-slot="5804833411" data-ad-format="auto"></ins>
        <script>(adsbygoogle = window.adsbygoogle || []).push({});</script>	
    </body>
</html>
