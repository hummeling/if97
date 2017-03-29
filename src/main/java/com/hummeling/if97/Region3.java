/*
 * Region3.java
 *
 * This file is part of IF97.
 *
 * IF97 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * IF97 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with IF97. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2017 Hummeling Engineering BV (www.hummeling.com)
 */
package com.hummeling.if97;

import static com.hummeling.if97.IF97.*;
import static java.lang.Double.NaN;
import static java.lang.StrictMath.*;

/**
 * Region 3.
 *
 * @author Ralph Hummeling (<a
 * href="http://www.hummeling.com">www.hummeling.com</a>)
 */
final class Region3 extends Region {

    private final double rhoRef, n1;
    private final double[][] IJnPi, IJnOmegaA, IJnOmegaB, IJnOa, IJnOb, IJnPiA, IJnPiB, IJnThetaA, IJnThetaB, IJnTa, IJnTb;

    Region3() {

        super("Region 3");

        rhoRef = 322;
        n1 = 0.10658070028513e1;
        IJnPi = new double[][]{
            {0, 0, -0.15732845290239e2},
            {0, 1, 00.20944396974307e2},
            {0, 2, -0.76867707878716e1},
            {0, 7, 00.26185947787954e1},
            {0, 10, -.28080781148620e1},
            {0, 12, 0.12053369696517e1},
            {0, 23, -.84566812812502e-2},
            {1, 2, -0.12654315477714e1},
            {1, 6, -0.11524407806681e1},
            {1, 15, 0.88521043984318},
            {1, 17, -.64207765181607},
            {2, 0, 00.38493460186671},
            {2, 2, -0.85214708824206},
            {2, 6, 00.48972281541877e1},
            {2, 7, -0.30502617256965e1},
            {2, 22, 0.39420536879154e-1},
            {2, 26, 0.12558408424308},
            {3, 0, -0.27999329698710},
            {3, 2, 00.13899799569460e1},
            {3, 4, -0.20189915023570e1},
            {3, 16, -.82147637173963e-2},
            {3, 26, -.47596035734923},
            {4, 0, 00.43984074473500e-1},
            {4, 2, -0.44476435428739},
            {4, 4, 00.90572070719733},
            {4, 26, 0.70522450087967},
            {5, 1, 00.10770512626332},
            {5, 3, -0.32913623258954},
            {5, 26, -.50871062041158},
            {6, 0, -0.22175400873096e-1},
            {6, 2, 00.94260751665092e-1},
            {6, 26, 0.16436278447961},
            {7, 2, -0.13503372241348e-1},
            {8, 26, -.14834345352472e-1},
            {9, 2, 00.57922953628084e-3},
            {9, 26, 0.32308904703711e-2},
            {10, 0, 0.80964802996215e-4},
            {10, 1, -.16557679795037e-3},
            {11, 26, -.44923899061815e-4}};
        IJnOmegaA = new double[][]{
            {-12, 6, 5.29944062966028e-3},
            {-12, 8, -1.70099690234461e-1},
            {-12, 12, 1.11323814312927e1},
            {-12, 18, -2.17898123145125e3},
            {-10, 4, -5.06061827980875e-4},
            {-10, 7, 5.56495239685324e-1},
            {-10, 10, -9.43672726094016},
            {-8, 5, -2.97856807561527e-1},
            {-8, 12, 9.39353943717186e1},
            {-6, 3, 1.92944939465981e-2},
            {-6, 4, 4.21740664704763e-1},
            {-6, 22, -3.68914126282330e6},
            {-4, 2, -7.37566847600639e-3},
            {-4, 3, -3.54753242424366e-1},
            {-3, 7, -1.99768169338727},
            {-2, 3, 1.15456297059049},
            {-2, 16, 5.68366875815960e3},
            {-1, 0, 8.08169540124668e-3},
            {-1, 1, 1.72416341519307e-1},
            {-1, 2, 1.04270175292927},
            {-1, 3, -2.97691372792847e-1},
            {0, 0, 5.60394465163593e-1},
            {0, 1, 2.75234661176914e-1},
            {1, 0, -1.48347894866012e-1},
            {1, 1, -6.51142513478515e-2},
            {1, 2, -2.92468715386302},
            {2, 0, 6.64876096952665e-2},
            {2, 2, 3.52335014263844},
            {3, 0, -1.46340792313332e-2},
            {4, 2, -2.24503486668184},
            {5, 2, 1.10533464706142},
            {8, 2, -4.08757344495612e-2}};
        IJnOmegaB = new double[][]{
            {-12, 0, -2.25196934336318e-9},
            {-12, 1, 1.40674363313486e-8},
            {-8, 0, 2.33784085280560e-6},
            {-8, 1, -3.31833715229001e-5},
            {-8, 3, 1.07956778514318e-3},
            {-8, 6, -2.71382067378863e-1},
            {-8, 7, 1.07202262490333},
            {-8, 8, -8.53821329075382e-1},
            {-6, 0, -2.15214194340526e-5},
            {-6, 1, 7.69656088222730e-4},
            {-6, 2, -4.31136580433864e-3},
            {-6, 5, 4.53342167309331e-1},
            {-6, 6, -5.07749535873652e-1},
            {-6, 10, -1.00475154528389e2},
            {-4, 3, -2.19201924648793e-1},
            {-4, 6, -3.21087965668917},
            {-4, 10, 6.07567815637771e2},
            {-3, 0, 5.57686450685932e-4},
            {-3, 2, 1.87499040029550e-1},
            {-2, 1, 9.05368030448107e-3},
            {-2, 2, 2.85417173048685e-1},
            {-1, 0, 3.29924030996098e-2},
            {-1, 1, 2.39897419685483e-1},
            {-1, 4, 4.82754995951394},
            {-1, 5, -1.18035753702231e1},
            {0, 0, 1.69490044091791e-1},
            {1, 0, -1.79967222507787e-2},
            {1, 1, 3.71810116332674e-2},
            {2, 2, -5.36288335065096e-2},
            {2, 6, 1.60697101092520}};
        IJnOa = new double[][]{
            {-12, 10, .795544074093975e2},
            {-12, 12, -.238261242984590e4},
            {-12, 14, .176813100617787e5},
            {-10, 4, -.110524727080379e-2},
            {-10, 8, -.153213833655326e2},
            {-10, 10, .297544599376982e3},
            {-10, 20, -.350315206871242e8},
            {-8, 5, .277513761062119},
            {-8, 6, -.523964271036888},
            {-8, 14, -.148011182995403e6},
            {-8, 16, .160014899374266e7},
            {-6, 28, .170802322663427e13},
            {-5, 1, .246866996006494e-3},
            {-4, 5, .165326084797980e1},
            {-3, 2, -.118008384666987},
            {-3, 4, .253798642355900e1},
            {-2, 3, .965127704669424},
            {-2, 8, -.282172420532826e2},
            {-1, 1, .203224612353823},
            {-1, 2, .110648186063513e1},
            {0, 0, .526127948451280},
            {0, 1, .277000018736321},
            {0, 3, .108153340501132e1},
            {1, 0, -.744127885357893e-1},
            {2, 0, .164094443541384e-1},
            {4, 2, -.680468275301065e-1},
            {5, 2, .257988576101640e-1},
            {6, 0, -.145749861944416e-3}};
        IJnOb = new double[][]{
            {-12, 0, .591599780322238e-4},
            {-12, 1, -.185465997137856e-2},
            {-12, 2, .104190510480013e-1},
            {-12, 3, .598647302038590e-2},
            {-12, 5, -.771391189901699},
            {-12, 6, .172549765557036e1},
            {-10, 0, -.467076079846526e-3},
            {-10, 1, .134533823384439e-1},
            {-10, 2, -.808094336805495e-1},
            {-10, 4, .508139374365767},
            {-8, 0, .128584643361683e-2},
            {-5, 1, -.163899353915435e1},
            {-5, 2, .586938199318063e1},
            {-5, 3, -.292466667918613e1},
            {-4, 0, -.614076301499537e-2},
            {-4, 1, .576199014049172e1},
            {-4, 2, -.121613320606788e2},
            {-4, 3, .167637540957944e1},
            {-3, 1, -.744135838773463e1},
            {-2, 0, .378168091437659e-1},
            {-2, 1, .401432203027688e1},
            {-2, 2, .160279837479185e2},
            {-2, 3, .317848779347728e1},
            {-2, 4, -.358362310304853e1},
            {-2, 12, -.115995260446827e7},
            {0, 0, .199256573577909},
            {0, 1, -.122270624794624},
            {0, 2, -.191449143716586e2},
            {1, 0, -.150448002905284e-1},
            {1, 2, .146407900162154e2},
            {2, 2, -.327477787188230e1}};
        IJnPiA = new double[][]{
            {0, 0, .770889828326934e1},
            {0, 1, -.260835009128688e2},
            {0, 5, .267416218930389e3},
            {1, 0, .172221089496844e2},
            {1, 3, -.293542332145970e3},
            {1, 4, .614135601882478e3},
            {1, 8, -.610562757725674e5},
            {1, 14, -.651272251118219e8},
            {2, 6, .735919313521937e5},
            {2, 16, -.116646505914191e11},
            {3, 0, .355267086434461e2},
            {3, 2, -.596144543825955e3},
            {3, 3, -.475842430145708e3},
            {4, 0, .696781965359503e2},
            {4, 1, .335674250377312e3},
            {4, 4, .250526809130882e5},
            {4, 5, .146997380630766e6},
            {5, 28, .538069315091534e20},
            {6, 28, .143619827291346e22},
            {7, 24, .364985866165994e20},
            {8, 1, -.254741561156775e4},
            {10, 32, .240120197096563e28},
            {10, 36, -.393847464679496e30},
            {14, 22, .147073407024852e25},
            {18, 28, -.426391250432059e32},
            {20, 36, .194509340621077e39},
            {22, 16, .666212132114896e24},
            {22, 28, .706777016552858e34},
            {24, 36, .175563621975576e42},
            {28, 16, .108408607429124e29},
            {28, 36, .730872705175151e44},
            {32, 10, .159145847398870e25},
            {32, 28, .377121605943324e41}};
        IJnPiB = new double[][]{
            {-12, 2, .125244360717979e-12},
            {-12, 10, -.126599322553713e-1},
            {-12, 12, .506878030140626e1},
            {-12, 14, .317847171154202e2},
            {-12, 20, -.391041161399932e6},
            {-10, 2, -.975733406392044e-10},
            {-10, 10, -.186312419488279e2},
            {-10, 14, .510973543414101e3},
            {-10, 18, .373847005822362e6},
            {-8, 2, .299804024666572e-7},
            {-8, 8, .200544393820342e2},
            {-6, 2, -.498030487662829e-5},
            {-6, 6, -.102301806360030e2},
            {-6, 7, .552819126990325e2},
            {-6, 8, -.206211367510878e3},
            {-5, 10, -.794012232324823e4},
            {-4, 4, .782248472028153e1},
            {-4, 5, -.586544326902468e2},
            {-4, 8, .355073647696481e4},
            {-3, 1, -.115303107290162e-3},
            {-3, 3, -.175092403171802e1},
            {-3, 5, .257981687748160e3},
            {-3, 6, -.727048374179467e3},
            {-2, 0, .121644822609198e-3},
            {-2, 1, .393137871762692e-1},
            {-1, 0, .704181005909296e-2},
            {0, 3, -.829108200698110e2},
            {2, 0, -.265178818131250},
            {2, 1, .137531682453991e2},
            {5, 0, -.522394090753046e2},
            {6, 1, .240556298941048e4},
            {8, 1, -.227361631268929e5},
            {10, 1, .890746343932567e5},
            {14, 3, -.239234565822486e8},
            {14, 7, .568795808129714e10}};
        IJnThetaA = new double[][]{
            {-12, 0, -1.33645667811215e-7},
            {-12, 1, 4.55912656802978e-6},
            {-12, 2, -1.46294640700979e-5},
            {-12, 6, 6.39341312970080e-3},
            {-12, 14, 3.72783927268847e2},
            {-12, 16, -7.18654377460447e3},
            {-12, 20, 5.73494752103400e5},
            {-12, 22, -2.67569329111439e6},
            {-10, 1, -3.34066283302614e-5},
            {-10, 5, -2.45479214069597e-2},
            {-10, 12, 4.78087847764996e1},
            {-8, 0, 7.64664131818904e-6},
            {-8, 2, 1.28350627676972e-3},
            {-8, 4, 1.71219081377331e-2},
            {-8, 10, -8.51007304583213},
            {-5, 2, -1.36513461629781e-2},
            {-3, 0, -3.84460997596657e-6},
            {-2, 1, 3.37423807911655e-3},
            {-2, 3, -5.51624873066791e-1},
            {-2, 4, 7.29202277107470e-1},
            {-1, 0, -9.92522757376041e-3},
            {-1, 2, -1.19308831407288e-1},
            {0, 0, 7.93929190615421e-1},
            {0, 1, 4.54270731799386e-1},
            {1, 1, 2.09998591259910e-1},
            {3, 0, -6.42109823904738e-3},
            {3, 1, -2.35155868604540e-2},
            {4, 0, 2.52233108341612e-3},
            {4, 3, -7.64885133368119e-3},
            {10, 4, 1.36176427574291e-2},
            {12, 5, -1.33027883575669e-2}};
        IJnThetaB = new double[][]{
            {-12, 0, 3.23254573644920e-5},
            {-12, 1, -1.27575556587181e-4},
            {-10, 0, -4.75851877356068e-4},
            {-10, 1, 1.56183014181602e-3},
            {-10, 5, 1.05724860113781e-1},
            {-10, 10, -8.58514221132534e1},
            {-10, 12, 7.24140095480911e2},
            {-8, 0, 2.96475810273257e-3},
            {-8, 1, -5.92721983365988e-3},
            {-8, 2, -1.26305422818666e-2},
            {-8, 4, -1.15716196364853e-1},
            {-8, 10, 8.49000969739595e1},
            {-6, 0, -1.08602260086615e-2},
            {-6, 1, 1.54304475328851e-2},
            {-6, 2, 7.50455441524466e-2},
            {-4, 0, 2.52520973612982e-2},
            {-4, 1, -6.02507901232996e-2},
            {-3, 5, -3.07622221350501},
            {-2, 0, -5.74011959864879e-2},
            {-2, 4, 5.03471360939849},
            {-1, 2, -9.25081888584834e-1},
            {-1, 4, 3.91733882917546},
            {-1, 6, -7.73146007130190e1},
            {-1, 10, 9.49308762098587e3},
            {-1, 14, -1.41043719679409e6},
            {-1, 16, 8.49166230819026e6},
            {0, 0, 8.61095729446704e-1},
            {0, 2, 3.23346442811720e-1},
            {1, 1, 8.73281936020439e-1},
            {3, 1, -4.36653048526683e-1},
            {5, 1, 2.86596714529479e-1},
            {6, 1, -1.31778331276228e-1},
            {8, 1, 6.76682064330275e-3}};
        IJnTa = new double[][]{
            {-12, 28, .150042008263875e10},
            {-12, 32, -.159397258480424e12},
            {-10, 4, .502181140217975e-3},
            {-10, 10, -.672057767855466e2},
            {-10, 12, .145058545404456e4},
            {-10, 14, -.823889534888890e4},
            {-8, 5, -.154852214233853},
            {-8, 7, .112305046746695e2},
            {-8, 8, -.297000213482822e2},
            {-8, 28, .438565132635495e11},
            {-6, 2, .137837838635464e-2},
            {-6, 6, -.297478527157462e1},
            {-6, 32, .971777947349413e13},
            {-5, 0, -.571527767052398e-4},
            {-5, 14, .288307949778420e5},
            {-5, 32, -.744428289262703e14},
            {-4, 6, .128017324848921e2},
            {-4, 10, -.368275545889071e3},
            {-4, 36, .664768904779177e16},
            {-2, 1, .449359251958880e-1},
            {-2, 4, -.422897836099655e1},
            {-1, 1, -.240614376434179},
            {-1, 6, -.474341365254924e1},
            {0, 0, .724093999126110},
            {0, 1, .923874349695897},
            {0, 4, .399043655281015e1},
            {1, 0, .384066651868009e-1},
            {2, 0, -.359344365571848e-2},
            {2, 3, -.735196448821653},
            {3, 2, .188367048396131},
            {8, 0, .141064266818704e-3},
            {8, 1, -.257418501496337e-2},
            {10, 2, .123220024851555e-2}};
        IJnTb = new double[][]{
            {-12, 1, .527111701601660},
            {-12, 3, -.401317830052742e2},
            {-12, 4, .153020073134484e3},
            {-12, 7, -.224799398218827e4},
            {-8, 0, -.193993484669048},
            {-8, 1, -.140467557893768e1},
            {-8, 3, .426799878114024e2},
            {-6, 0, .752810643416743},
            {-6, 2, .226657238616417e2},
            {-6, 4, -.622873556909932e3},
            {-5, 0, -.660823667935396},
            {-5, 1, .841267087271658},
            {-5, 2, -.253717501764397e2},
            {-5, 4, .485708963532948e3},
            {-5, 6, .880531517490555e3},
            {-4, 12, .265015592794626e7},
            {-3, 1, -.359287150025783},
            {-3, 6, -.656991567673753e3},
            {-2, 2, .241768149185367e1},
            {0, 0, .856873461222588},
            {2, 1, .655143675313458},
            {3, 1, -.213535213206406},
            {4, 0, .562974957606348e-2},
            {5, 24, -.316955725450471e15},
            {6, 0, -.699997000152457e-3},
            {8, 3, .119845803210767e-1},
            {12, 1, .193848122022095e-4},
            {14, 2, -.215095749182309e-4}};
    }

    private SubRegion getSubRegionS(double entropy) {

        return entropy > sc ? SubRegion.b : SubRegion.a;
    }

    private SubRegion getSubRegionPH(double pressure, double enthalpy) {

        return enthalpy < enthalpy3ab(pressure) ? SubRegion.a : SubRegion.b;
    }

    double enthalpy3ab(double pressure) {

        double out = 0, pi = pressure;
        double[] n = {
            0.201464004206875e4,
            0.374696550136983e1,
            -.219921901054187e-1,
            0.875131686009950e-4};

        for (int i = 0; i < n.length; i++) {
            out += n[i] * pow(pi, i);
        }
        return out;
    }

    /**
     * Dimensionless specific volume for subregion 3a.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless specific enthalpy [kJ/kg]
     * @return dimensionless specific volume
     */
    private double omegaA(double pi, double eta) {

        double out = 0;

        for (double[] ijn : IJnOmegaA) {
            out += ijn[2] * pow(pi + 0.128, ijn[0]) * pow(eta - 0.727, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless specific volume for subregion 3b.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless specific enthalpy [kJ/kg]
     * @return dimensionless specific volume
     */
    private double omegaB(double pi, double eta) {

        double out = 0;

        for (double[] ijn : IJnOmegaB) {
            out += ijn[2] * pow(pi + 0.0661, ijn[0]) * pow(eta - 0.720, ijn[1]);
        }
        return out;
    }

    /**
     * Specific Helmholtz free energy.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phi(double delta, double tau) {

        double out = n1 * log(delta);

        for (double[] ijn : IJnPi) {
            out += ijn[2] * pow(delta, ijn[0]) * pow(tau, ijn[1]);
        }
        return out;
    }

    /**
     * First partial derivative with respect to delta.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phiDelta(double delta, double tau) {

        double out = n1 / delta;

        for (double[] ijn : IJnPi) {
            out += ijn[2] * ijn[0] * pow(delta, ijn[0] - 1) * pow(tau, ijn[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to delta.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phiDeltaDelta(double delta, double tau) {

        double out = -n1 / (delta * delta);

        for (double[] ijn : IJnPi) {
            out += ijn[2] * ijn[0] * (ijn[0] - 1) * pow(delta, ijn[0] - 2) * pow(tau, ijn[1]);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to delta & tau.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phiDeltaTau(double delta, double tau) {

        double out = 0;

        for (double[] ijn : IJnPi) {
            out += ijn[2] * ijn[0] * pow(delta, ijn[0] - 1) * ijn[1] * pow(tau, ijn[1] - 1);
        }
        return out;
    }

    /**
     * First partial derivative with respect to tau.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phiTau(double delta, double tau) {

        double out = 0;

        for (double[] ijn : IJnPi) {
            out += ijn[2] * pow(delta, ijn[0]) * ijn[1] * pow(tau, ijn[1] - 1);
        }
        return out;
    }

    /**
     * Second partial derivative with respect to tau.
     *
     * @param delta dimensionless density [kg/m3]
     * @param tau dimensionless temperature [K]
     * @return
     */
    private double phiTauTau(double delta, double tau) {

        double out = 0;

        for (double[] ijn : IJnPi) {
            out += ijn[2] * pow(delta, ijn[0]) * ijn[1] * (ijn[1] - 1) * pow(tau, ijn[1] - 2);
        }
        return out;
    }

    /**
     * Dimensionless pressure for subregion 3a.
     *
     * @param eta dimensionless specific enthalpy
     * @param sigma dimensionless specific entropy
     * @return dimensionless pressure
     */
    private double piA(double eta, double sigma) {

        double out = 0;
        double[] x = {eta - 1.01, sigma - 0.75};

        for (double[] ijn : IJnPiA) {
            out += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless pressure for subregion 3b.
     *
     * @param eta dimensionless specific enthalpy
     * @param sigma dimensionless specific entropy
     * @return dimensionless pressure
     */
    private double piB(double eta, double sigma) {

        double out = 0;
        double[] x = {eta - 0.681, sigma - 0.792};

        for (double[] ijn : IJnPiB) {
            out += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return 1 / out;
    }

    /**
     * Dimensionless temperature for subregion 3a.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless specific enthalpy [kJ/kg]
     * @return dimensionless temperature
     */
    private double thetaA(double pi, double eta) {

        double out = 0;

        for (double[] ijn : IJnThetaA) {
            out += ijn[2] * pow(pi + 0.240, ijn[0]) * pow(eta - 0.615, ijn[1]);
        }
        return out;
    }

    /**
     * Dimensionless temperature for subregion 3b.
     *
     * @param pi dimensionless pressure [MPa]
     * @param eta dimensionless specific enthalpy [kJ/kg]
     * @return dimensionless temperature
     */
    private double thetaB(double pi, double eta) {

        double out = 0;

        for (double[] ijn : IJnThetaB) {
            out += ijn[2] * pow(pi + 0.298, ijn[0]) * pow(eta - 0.720, ijn[1]);
        }
        return out;
    }

    @Override
    double isobaricCubicExpansionCoefficientPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return isobaricCubicExpansionCoefficientRhoT(rho, temperature);
    }

    double isobaricCubicExpansionCoefficientRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature,
                phiDelta = phiDelta(delta, tau);

        return (phiDelta - tau * phiDeltaTau(delta, tau)) / (2 * phiDelta + delta * phiDeltaDelta(delta, tau)) / temperature;
    }

    @Override
    double isothermalCompressibilityPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return isothermalCompressibilityRhoT(rho, temperature);
    }

    double isothermalCompressibilityRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature;

        return 1e3 / (2 * delta * phiDelta(delta, tau) + delta * delta * phiDeltaDelta(delta, tau)) / (density * R * temperature);
    }

    double isothermalStressCoefficientRhoT(double rho, double T) {

        double delta = rho / rhoc,
                tau = Tc / T;

        return rho * (2 + delta * phiDeltaDelta(delta, tau) / phiDelta(delta, tau));
    }

    @Override
    double pressureHS(double h, double s) {

        switch (getSubRegionS(s)) {
            case a:
                return piA(h / 2300, s / 4.4) * 99;

            case b:
                return piB(h / 2800, s / 5.3) * 16.6;

            default:
                return NaN;
        }
    }

    double pressureRhoT(double rho, double T) {

        double delta = rho / rhoc;

        return delta * phiDelta(delta, Tc / T) * rho * R * T / 1e3;
    }

    /**
     * alpha p
     *
     * @param rho
     * @param T
     * @return
     */
    double relativePressureCoefficientRhoT(double rho, double T) {

        double delta = rho / rhoc,
                tau = Tc / T;

        return (1 - tau * phiDeltaTau(delta, tau) / phiDelta(delta, tau)) / T;
    }

    @Override
    double specificEnthalpyPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return specificEnthalpyRhoT(rho, temperature);
    }

    double specificEnthalpyRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature;

        return (tau * phiTau(delta, tau) + delta * phiDelta(delta, tau)) * R * temperature;
    }

    @Override
    double specificEntropyPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return specificEntropyRhoT(rho, temperature);
    }

    @Override
    double specificEntropyRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature;

        return (tau * phiTau(delta, tau) - phi(delta, tau)) * R;
    }

    @Override
    double specificGibbsFreeEnergyPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature),
                delta = rho / rhoc,
                tau = Tc / temperature;

        return phi(delta, tau) * R * temperature + pressure / rho;
    }

    @Override
    double specificInternalEnergyPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return specificInternalEnergyRhoT(rho, temperature);
    }

    double specificInternalEnergyRhoT(double density, double temperature) {

        double tau = Tc / temperature;

        return tau * phiTau(density / rhoc, tau) * R * temperature;
    }

    @Override
    double specificIsobaricHeatCapacityPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return specificIsobaricHeatCapacityRhoT(rho, temperature);
    }

    double specificIsobaricHeatCapacityRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature,
                phiDelta = phiDelta(delta, tau),
                x = delta * phiDelta - delta * tau * phiDeltaTau(delta, tau);

        return (-tau * tau * phiTauTau(delta, tau) + x * x / (2 * delta * phiDelta + delta * delta * phiDeltaDelta(delta, tau))) * R;
    }

    @Override
    double specificIsochoricHeatCapacityPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return specificIsochoricHeatCapacityRhoT(rho, temperature);
    }

    double specificIsochoricHeatCapacityRhoT(double density, double temperature) {

        double tau = Tc / temperature;

        return -tau * tau * phiTauTau(density / rhoc, tau) * R;
    }

    @Override
    double specificVolumeHS(double enthalpy, double entropy) {

        return specificVolumePH(pressureHS(enthalpy, entropy), enthalpy);
    }

    @Override
    double specificVolumePH(double pressure, double enthalpy) { //TODO Explain why specificVolumePT() is called instead.

        double pi = pressure / 100;

        switch (getSubRegionPH(pressure, enthalpy)) {
            case a:
                return omegaA(pi, enthalpy / 2100) * 0.0028;

            case b:
                return omegaB(pi, enthalpy / 2800) * 0.0088;

            default:
                return NaN;
        }
    }

    @Override
    double specificVolumePS(double pressure, double entropy) {

        double omega = 0;
        double[] x = null;
        double[][] IJn = null;

        switch (getSubRegionS(entropy)) {
            case a:
                x = new double[]{pressure / 100 + 0.187, entropy / 4.4 - 0.755, 0.0028};
                IJn = IJnOa;
                break;

            case b:
                x = new double[]{pressure / 100 + 0.298, entropy / 5.3 - 0.816, 0.0088};
                IJn = IJnOb;
                break;
        }
        for (double[] ijn : IJn) {
            omega += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return omega * x[2];
    }

    @Override
    double specificVolumePT(double p, double T) {

        double dTheta_dPi = 3.727888004,
                omega = 0,
                pi = p,
                p3cd = 19.00881189,
                theta,
                logPi = log(pi),
                ps643 = REGION4.saturationPressureT(643.15),
                Ts = REGION4.saturationTemperatureP(p);

        double[][] In;

        /*
         Boundary Equations
         */
        theta = 0;
        In = new double[][]{
            {0, 0.154793642129415e4},
            {1, -.187661219490113e3},
            {2, 0.213144632222112e2},
            {-1, -.191887498864292e4},
            {-2, 0.918419702359447e3}};

        for (double[] in : In) {
            theta += in[1] * pow(logPi, in[0]);
        }
        double T3ab = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.585276966696349e3},
            {1, 0.278233532206915e1},
            {2, -.127283549295878e-1},
            {3, 0.159090746562729e-3}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3cd = theta,
                T3ef = dTheta_dPi * (pi - pc) + Tc;

        theta = 0;
        In = new double[][]{
            {0, -.249284240900418e5},
            {1, 0.428143584791546e4},
            {2, -.269029173140130e3},
            {3, 0.751608051114157e1},
            {4, -.787105249910383e-1}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3gh = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.584814781649163e3},
            {1, -.616179320924617},
            {2, 0.260763050899562},
            {3, -.587071076864459e-2},
            {4, 0.515308185433082e-4}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3ij = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.617229772068439e3},
            {1, -.770600270141675e1},
            {2, 0.697072596851896},
            {3, -.157391839848015e-1},
            {4, 0.137897492684194e-3}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3jk = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.535339483742384e3},
            {1, 0.761978122720128e1},
            {2, -.158365725441648},
            {3, 0.192871054508108e-2}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3mn = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.969461372400213e3},
            {1, -.332500170441278e3},
            {2, 0.642859598466067e2},
            {-1, 0.773845935768222e3},
            {-2, -.152313732937084e4}};

        for (double[] in : In) {
            theta += in[1] * pow(logPi, in[0]);
        }
        double T3op = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.565603648239126e3},
            {1, 0.529062258221222e1},
            {2, -.102020639611016},
            {3, 0.122240301070145e-2}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3qu = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.584561202520006e3},
            {1, -.102961025163669e1},
            {2, 0.243293362700452},
            {3, -.294905044740799e-2}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3rx = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.528199646263062e3},
            {1, 0.890579602135307e1},
            {2, -.222814134903755},
            {3, 0.286791682263697e-2}};

        for (double[] in : In) {
            theta += in[1] * pow(pi, in[0]);
        }
        double T3uv = theta;

        theta = 0;
        In = new double[][]{
            {0, 0.728052609145380e1},
            {1, 0.973505869861952e2},
            {2, 0.147370491183191e2},
            {-1, 0.329196213998375e3},
            {-2, 0.873371668682417e3}};

        for (double[] in : In) {
            theta += in[1] * pow(logPi, in[0]);
        }
        double T3wx = theta;


        /*
         Subregions
         */
        SubRegion subRegion;

        //if (22.5 >= p && p > ps643 && T3rx > T && T > T3qu) {
        if (T3qu <= T && T <= T3rx && ps643 <= p && p <= 22.5) {
            /*
             Auxiliary Equations for the Near-Critical Region (p.126)
             */
            if (p <= pc) {
                if (T <= Ts) {
                    if (21.93161551 < p) {
                        subRegion = T <= T3uv ? SubRegion.u : SubRegion.y;
                    } else {
                        subRegion = SubRegion.u;
                    }
                } else if (21.90096265 < p) {
                    subRegion = T <= T3wx ? SubRegion.z : SubRegion.x;
                } else {
                    subRegion = SubRegion.x;
                }

            } else if (p <= 22.11) {
                if (T > T3wx) {
                    subRegion = SubRegion.x;

                } else if (T > T3ef) {
                    subRegion = SubRegion.z;

                } else if (T > T3uv) {
                    subRegion = SubRegion.y;

                } else {
                    subRegion = SubRegion.u;
                }
            } else if (T > T3wx) {
                subRegion = SubRegion.x;

            } else if (T > T3ef) {
                subRegion = SubRegion.w;

            } else if (T > T3uv) {
                subRegion = SubRegion.v;

            } else {
                subRegion = SubRegion.u;
            }

        } else if (40 < p) {
            subRegion = T <= T3ab ? SubRegion.a : SubRegion.b;

        } else if (25 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= T3ab) {
                subRegion = SubRegion.d;

            } else if (T <= T3ef) {
                subRegion = SubRegion.e;

            } else {
                subRegion = SubRegion.f;
            }

        } else if (23.5 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= T3gh) {
                subRegion = SubRegion.g;

            } else if (T <= T3ef) {
                subRegion = SubRegion.h;

            } else if (T <= T3ij) {
                subRegion = SubRegion.i;

            } else if (T <= T3jk) {
                subRegion = SubRegion.j;

            } else {
                subRegion = SubRegion.k;
            }

        } else if (23 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= T3gh) {
                subRegion = SubRegion.l;

            } else if (T <= T3ef) {
                subRegion = SubRegion.h;

            } else if (T <= T3ij) {
                subRegion = SubRegion.i;

            } else if (T <= T3jk) {
                subRegion = SubRegion.j;

            } else {
                subRegion = SubRegion.k;
            }

        } else if (22.5 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= T3gh) {
                subRegion = SubRegion.l;

            } else if (T <= T3mn) {
                subRegion = SubRegion.m;

            } else if (T <= T3ef) {
                subRegion = SubRegion.n;

            } else if (T <= T3op) {
                subRegion = SubRegion.o;

            } else if (T <= T3ij) {
                subRegion = SubRegion.p;

            } else if (T <= T3jk) {
                subRegion = SubRegion.j;

            } else {
                subRegion = SubRegion.k;
            }

        } else if (ps643 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= T3qu) {
                subRegion = SubRegion.q;

            } else if (T3rx < T && T <= T3jk) {
                subRegion = SubRegion.r;

            } else {
                subRegion = SubRegion.k;
            }

        } else if (20.5 < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= Ts) {
                subRegion = SubRegion.s;

            } else if (T <= T3jk) {
                subRegion = SubRegion.r;

            } else {
                subRegion = SubRegion.k;
            }

        } else if (p3cd < p) {
            if (T <= T3cd) {
                subRegion = SubRegion.c;

            } else if (T <= Ts) {
                subRegion = SubRegion.s;

            } else {
                subRegion = SubRegion.t;
            }

        } else if (ps13 < p) {
            subRegion = T <= Ts ? SubRegion.c : SubRegion.t;

        } else {
            return NaN;
        }

        pi = p / subRegion.pRed;
        theta = T / subRegion.Tred;

        /*
         Backward Equation
         */
        double[] x;

        switch (subRegion) {
            case n:
                x = new double[]{pi - subRegion.A, theta - subRegion.B};

                for (double[] ijn : subRegion.IJn) {
                    omega += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
                }
                return exp(omega) * subRegion.nuRed;

            default:
                x = new double[]{
                    pow(pi - subRegion.A, subRegion.C),
                    pow(theta - subRegion.B, subRegion.D)
                };
                for (double[] ijn : subRegion.IJn) {
                    omega += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
                }
                return pow(omega, subRegion.E) * subRegion.nuRed;
        }
    }

    @Override
    double speedOfSoundPT(double pressure, double temperature) {

        double rho = 1 / specificVolumePT(pressure, temperature);

        return speedOfSoundRhoT(rho, temperature);
    }

    double speedOfSoundRhoT(double density, double temperature) {

        double delta = density / rhoc,
                tau = Tc / temperature,
                phiDelta = phiDelta(delta, tau),
                x = delta * phiDelta - delta * tau * phiDeltaTau(delta, tau);

        return sqrt((2 * delta * phiDelta + delta * delta * phiDeltaDelta(delta, tau) - x * x / (tau * tau * phiTauTau(delta, tau))) * 1e3 * R * temperature);
    }

    @Override
    double temperatureHS(double enthalpy, double entropy) {

        return temperaturePH(pressureHS(enthalpy, entropy), enthalpy);
    }

    @Override
    double temperaturePH(double pressure, double enthalpy) {

        switch (getSubRegionPH(pressure, enthalpy)) {
            case a:
                return thetaA(pressure / 100, enthalpy / 2300) * 760;

            case b:
                return thetaB(pressure / 100, enthalpy / 2800) * 860;

            default:
                return NaN;
        }
    }

    @Override
    double temperaturePS(double pressure, double entropy) {

        double theta = 0;
        double[] x = null;
        double[][] IJn = null;

        switch (getSubRegionS(entropy)) {
            case a:
                x = new double[]{pressure / 100 + 0.240, entropy / 4.4 - 0.703, 760};
                IJn = IJnTa;
                break;

            case b:
                x = new double[]{pressure / 100 + 0.760, entropy / 5.3 - 0.818, 860};
                IJn = IJnTb;
                break;
        }
        for (double[] ijn : IJn) {
            theta += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
        }
        return theta * x[2];
    }

    @Override
    double vapourFractionHS(double enthalpy, double entropy) {

        return NaN;
    }

    @Override
    double vapourFractionPH(double pressure, double enthalpy) {

        return NaN;
    }

    @Override
    double vapourFractionPS(double pressure, double entropy) {

        return NaN;
    }

    @Override
    double vapourFractionTS(double temperature, double entropy) {

        return NaN;
    }

    enum SubRegion {

        a(0.0024, 100, 760, 0.085, 0.817, 1, 1, 1, new double[][]{
            {-12, 5, 00.110879558823853e-2},
            {-12, 10, 0.572616740810616e3},
            {-12, 12, -.767051948380852e5},
            {-10, 5, -0.253321069529674e-1},
            {-10, 10, 0.628008049345689e4},
            {-10, 12, 0.234105654131876e6},
            {-8, 5, 0.216867826045856},
            {-8, 8, -.156237904341963e3},
            {-8, 10, -.269893956176613e5},
            {-6, 1, -.180407100085505e-3},
            {-5, 1, 0.116732227668261e-2},
            {-5, 5, 0.266987040856040e2},
            {-5, 10, .282776617243286e5},
            {-4, 8, -.242431520029523e4},
            {-3, 0, 0.435217323022733e-3},
            {-3, 1, -.122494831387411e-1},
            {-3, 3, 0.179357604019989e1},
            {-3, 6, 0.442729521058314e2},
            {-2, 0, -.593223489018342e-2},
            {-2, 2, 0.453186261685774},
            {-2, 3, 0.135825703129140e1},
            {-1, 0, 0.408748415856745e-1},
            {-1, 1, 0.474686397863312},
            {-1, 2, 0.118646814997915e1},
            {0, 0, 0.546987265727549},
            {0, 1, 0.195266770452643},
            {1, 0, -.502268790869663e-1},
            {1, 2, -.369645308193377},
            {2, 0, 0.633828037528420e-2},
            {2, 2, 0.797441793901017e-1}}),
        b(0.0041, 100, 860, 0.280, 0.779, 1, 1, 1, new double[][]{
            {-12, 10, -0.827670470003621e-1},
            {-12, 12, 0.416887126010565e2},
            {-10, 8, 0.483651982197059e-1},
            {-10, 14, -0.291032084950276e5},
            {-8, 8, -0.111422582236948e3},
            {-6, 5, -0.202300083904014e-1},
            {-6, 6, 0.294002509338515e3},
            {-6, 8, 0.140244997609658e3},
            {-5, 5, -0.344384158811459e3},
            {-5, 8, 0.361182452612149e3},
            {-5, 10, -0.140699677420738e4},
            {-4, 2, -0.202023902676481e-2},
            {-4, 4, 0.171346792457471e3},
            {-4, 5, -0.425597804058632e1},
            {-3, 0, 0.691346085000334e-5},
            {-3, 1, 0.151140509678925e-2},
            {-3, 2, -0.416375290166236e-1},
            {-3, 3, -0.413754957011042e2},
            {-3, 5, -0.506673295721637e2},
            {-2, 0, -0.572212965569023e-3},
            {-2, 2, 0.608817368401785e1},
            {-2, 5, 0.239600660256161e2},
            {-1, 0, 0.122261479925384e-1},
            {-1, 2, 0.216356057692938e1},
            {0, 0, 0.3981989034},
            {0, 1, -0.1168928278},
            {1, 0, -0.1028459194},
            {1, 2, -0.4926766376},
            {2, 0, 0.655540456406790e-1},
            {3, 2, -0.2404625351},
            {4, 0, -0.269798180310075e-1},
            {4, 1, 0.128369435967012}}),
        c(0.0022, 40, 690, 0.259, 0.903, 1, 1, 1, new double[][]{
            {-12, 6, 0.311967788763030e1},
            {-12, 8, 0.276713458847564e5},
            {-12, 10, 0.322583103403269e8},
            {-10, 6, -0.342416065095363e3},
            {-10, 8, -0.899732529907377e6},
            {-10, 10, -0.793892049821251e8},
            {-8, 5, 0.953193003217388e2},
            {-8, 6, 0.229784742345072e4},
            {-8, 7, 0.175336675322499e6},
            {-6, 8, 0.791214365222792e7},
            {-5, 1, 0.319933345844209e-4},
            {-5, 4, -0.659508863555767e2},
            {-5, 7, -0.833426563212851e6},
            {-4, 2, 0.645734680583292e-1},
            {-4, 8, -0.382031020570813e7},
            {-3, 0, 0.406398848470079e-4},
            {-3, 3, 0.310327498492008e2},
            {-2, 0, -0.892996718483724e-3},
            {-2, 4, 0.234604891591616e3},
            {-2, 5, 0.377515668966951e4},
            {-1, 0, 0.158646812591361e-1},
            {-1, 1, 0.7079063362},
            {-1, 2, 0.126016225146570e2},
            {0, 0, 0.7361436558},
            {0, 1, 0.676544269},
            {0, 2, -0.178100588189137e2},
            {1, 0, -0.1565319755},
            {1, 2, 0.117707430048158e2},
            {2, 0, 0.840143653860447e-1},
            {2, 1, -0.1864424675},
            {2, 3, -0.440170203949645e2},
            {2, 7, 0.123290423502494e7},
            {3, 0, -0.240650039730845e-1},
            {3, 7, -0.107077716660869e7},
            {8, 1, 0.438319858566475e-1}}),
        d(0.0029, 40, 690, 0.559, 0.939, 1, 1, 4, new double[][]{
            {-12, 4, -4.52484847171645e-10},
            {-12, 6, 3.15210389538801e-5},
            {-12, 7, -2.14991352047545e-3},
            {-12, 10, 5.08058874808345e2},
            {-12, 12, -1.27123036845932e7},
            {-12, 16, 1.15371133120497e12},
            {-10, 0, -1.97805728776273e-16},
            {-10, 2, 2.41554806033972e-11},
            {-10, 4, -1.56481703640525e-6},
            {-10, 6, 2.77211346836625e-3},
            {-10, 8, -2.03578994462286e1},
            {-10, 10, 1.44369489909053e6},
            {-10, 14, -4.11254217946539e10},
            {-8, 3, 6.23449786243773e-6},
            {-8, 7, -2.21774281146038e1},
            {-8, 8, -6.89315087933158e4},
            {-8, 10, -1.95419525060713e7},
            {-6, 6, 3.16373510564015e3},
            {-6, 8, 2.24040754426988e6},
            {-5, 1, -4.36701347922356e-6},
            {-5, 2, -4.04213852833996e-4},
            {-5, 5, -3.48153203414663e2},
            {-5, 7, -3.85294213555289e5},
            {-4, 0, 1.35203700099403e-7},
            {-4, 1, 1.34648383271089e-4},
            {-4, 7, 1.25031835351736e5},
            {-3, 2, 9.68123678455841e-2},
            {-3, 4, 2.25660517512438e2},
            {-2, 0, -1.90102435341872e-4},
            {-2, 1, -2.99628410819229e-2},
            {-1, 0, 5.00833915372121e-3},
            {-1, 1, 3.87842482998411e-1},
            {-1, 5, -1.38535367777182e3},
            {0, 0, 8.70745245971773e-1},
            {0, 2, 1.71946252068742},
            {1, 0, -3.26650121426383e-2},
            {1, 6, 4.98044171727877e3},
            {3, 0, 5.51478022765087e-3}}),
        //
        e(0.0032, 40, 710, 0.587, 0.918, 1, 1, 1, new double[][]{
            {-12, 14, 0.715815808404721e9},
            {-12, 16, -.114328360753449e12},
            {-10, 3, 0.376531002015720e-11},
            {-10, 6, -.903983668691157e-4},
            {-10, 10, 0.665695908836252e6},
            {-10, 14, 0.535364174960127e10},
            {-10, 16, 0.794977402335603e11},
            {-8, 7, 0.922230563421437e2},
            {-8, 8, -.142586073991215e6},
            {-8, 10, -.111796381424162e7},
            {-6, 6, 0.896121629640760e4},
            {-5, 6, -.669989239070491e4},
            {-4, 2, 0.451242538486834e-2},
            {-4, 4, -.339731325977713e2},
            {-3, 2, -.120523111552278e1},
            {-3, 6, 0.475992667717124e5},
            {-3, 7, -.266627750390341e6},
            {-2, 0, -.153314954386524e-3},
            {-2, 1, 0.305638404828265},
            {-2, 3, 0.123654999499486e3},
            {-2, 4, -.104390794213011e4},
            {-1, 0, -.157496516174308e-1},
            {0, 0, 0.685331118940253},
            {0, 1, 0.178373462873903e1},
            {1, 0, -.544674124878910},
            {1, 4, 0.204529931318843e4},
            {1, 6, -.228342359328752e5},
            {2, 0, 0.413197481515899},
            {2, 2, -.341931835910405e2}}),
        f(0.0064, 40, 730, 0.587, 0.891, 0.5, 1, 4, new double[][]{
            {0, -3, -.251756547792325e-7},
            {0, -2, 0.601307193668763e-5},
            {0, -1, -.100615977450049e-2},
            {0, 0, 0.999969140252192},
            {0, 1, 0.214107759236486e1},
            {0, 2, -.165175571959086e2},
            {1, -1, -.141987303638727e-2},
            {1, 1, 0.269251915156554e1},
            {1, 2, 0.349741815858722e2},
            {1, 3, -.300208695771783e2},
            {2, 0, -.131546288252539e1},
            {2, 1, -.839091277286169e1},
            {3, -5, 0.181545608337015e-9},
            {3, -2, -.591099206478909e-3},
            {3, 0, 0.152115067087106e1},
            {4, -3, 0.252956470663225e-4},
            {5, -8, 0.100726265203786e-14},
            {5, 1, -.149774533860650e1},
            {6, -6, -.793940970562969e-9},
            {7, -4, -.150290891264717e-3},
            {7, 1, 0.151205531275133e1},
            {10, -6, 0.470942606221652e-5},
            {12, -10, 0.195049710391712e-12},
            {12, -8, -.911627886266077e-8},
            {12, -4, 0.604374640201265e-3},
            {14, -12, -.225132933900136e-15},
            {14, -10, 0.610916973582981e-11},
            {14, -8, -.303063908043404e-6},
            {14, -6, -.137796070798409e-4},
            {14, -4, -.919296736666106e-3},
            {16, -10, 0.639288223132545e-9},
            {16, -8, .753259479898699e-6},
            {18, -12, -.400321478682929e-12},
            {18, -10, 0.756140294351614e-8},
            {20, -12, -.912082054034891e-11},
            {20, -10, -.237612381140539e-7},
            {20, -6, 0.269586010591874e-4},
            {22, -12, -.732828135157839e-10},
            {24, -12, 0.241995578306660e-9},
            {24, -4, -.405735532730322e-3},
            {28, -12, 0.189424143498011e-9},
            {32, -12, -.486632965074563e-9}}),
        g(0.0027, 25, 660, 0.872, 0.971, 1, 1, 4, new double[][]{
            {-12, 7, 0.412209020652996e-4},
            {-12, 12, -.114987238280587e7},
            {-12, 14, 0.948180885032080e10},
            {-12, 18, -.195788865718971e18},
            {-12, 22, 0.496250704871300e25},
            {-12, 24, -.105549884548496e29},
            {-10, 14, -.758642165988278e12},
            {-10, 20, -.922172769596101e23},
            {-10, 24, 0.725379072059348e30},
            {-8, 7, -.617718249205859e2},
            {-8, 8, 0.107555033344858e5},
            {-8, 10, -.379545802336487e8},
            {-8, 12, 0.228646846221831e12},
            {-6, 8, -.499741093010619e7},
            {-6, 22, -.280214310054101e31},
            {-5, 7, 0.104915406769586e7},
            {-5, 20, 0.613754229168619e28},
            {-4, 22, 0.802056715528378e32},
            {-3, 7, -.298617819828065e8},
            {-2, 3, -.910782540134681e2},
            {-2, 5, 0.135033227281565e6},
            {-2, 14, -.712949383408211e19},
            {-2, 24, -.104578785289542e37},
            {-1, 2, 0.304331584444093e2},
            {-1, 8, 0.593250797959445e10},
            {-1, 18, -.364174062110798e28},
            {0, 0, 0.921791403532461},
            {0, 1, -.337693609657471},
            {0, 2, -.724644143758508e2},
            {1, 0, -.110480239272601},
            {1, 1, 0.536516031875059e1},
            {1, 3, -.291441872156205e4},
            {3, 24, 0.616338176535305e40},
            {5, 22, -.120889175861180e39},
            {6, 12, 0.818396024524612e23},
            {8, 3, 0.940781944835829e9},
            {10, 0, -.367279669545448e5},
            {10, 6, -.837513931798655e16}}),
        h(0.0032, 25, 660, 0.898, 0.983, 1, 1, 4, new double[][]{
            {-12, 8, .561379678887577e-1},
            {-12, 12, .774135421587083e10},
            {-10, 4, .111482975877938e-8},
            {-10, 6, -.143987128208183e-2},
            {-10, 8, .193696558764920e4},
            {-10, 10, -.605971823585005e9},
            {-10, 14, .171951568124337e14},
            {-10, 16, -.185461154985145e17},
            {-8, 0, .387851168078010e-16},
            {-8, 1, -.395464327846105e-13},
            {-8, 6, -.170875935679023e3},
            {-8, 7, -.212010620701220e4},
            {-8, 8, .177683337348191e8},
            {-6, 4, .110177443629575e2},
            {-6, 6, -.234396091693313e6},
            {-6, 8, -.656174421999594e7},
            {-5, 2, .156362212977396e-4},
            {-5, 3, -.212946257021400e1},
            {-5, 4, .135249306374858e2},
            {-4, 2, .177189164145813},
            {-4, 4, .139499167345464e4},
            {-3, 1, -.703670932036388e-2},
            {-3, 2, -.152011044389648},
            {-2, 0, .981916922991113e-4},
            {-1, 0, .147199658618076e-2},
            {-1, 2, .202618487025578e2},
            {0, 0, .899345518944240},
            {1, 0, -.211346402240858},
            {1, 2, .249971752957491e2}}),
        //
        i(0.0041, 25, 660, 0.910, 0.984, 0.5, 1, 4, new double[][]{
            {0, 0, .106905684359136e1},
            {0, 1, -.148620857922333e1},
            {0, 10, .259862256980408e15},
            {1, -4, -.446352055678749e-11},
            {1, -2, -.566620757170032e-6},
            {1, -1, -.235302885736849e-2},
            {1, 0, -.269226321968839},
            {2, 0, .922024992944392e1},
            {3, -5, .357633505503772e-11},
            {3, 0, -.173942565562222e2},
            {4, -3, .700681785556229e-5},
            {4, -2, -.267050351075768e-3},
            {4, -1, -.231779669675624e1},
            {5, -6, -.753533046979752e-12},
            {5, -1, .481337131452891e1},
            {5, 12, -.223286270422356e22},
            {7, -4, -.118746004987383e-4},
            {7, -3, .646412934136496e-2},
            {8, -6, -.410588536330937e-9},
            {8, 10, .422739537057241e20},
            {10, -8, .313698180473812e-12},
            {12, -12, .164395334345040e-23},
            {12, -6, -.339823323754373e-5},
            {12, -4, -.135268639905021e-1},
            {14, -10, -.723252514211625e-14},
            {14, -8, .184386437538366e-8},
            {14, -4, -.463959533752385e-1},
            {14, 5, -.992263100376750e14},
            {18, -12, .688169154439335e-16},
            {18, -10, -.222620998452197e-10},
            {18, -8, -.540843018624083e-7},
            {18, -6, .345570606200257e-2},
            {18, 2, .422275800304086e11},
            {20, -12, -.126974478770487e-14},
            {20, -10, .927237985153679e-9},
            {22, -12, .612670812016489e-13},
            {24, -12, -.722693924063497e-11},
            {24, -8, -.383669502636822e-3},
            {32, -10, .374684572410204e-3},
            {32, -5, -.931976897511086e5},
            {36, -10, -.247690616026922e-1},
            {36, -8, .658110546759474e2}}),
        j(0.0054, 25, 670, 0.875, 0.964, 0.5, 1, 4, new double[][]{
            {0, -1, -.111371317395540e-3},
            {0, 0, .100342892423685e1},
            {0, 1, .530615581928979e1},
            {1, -2, .179058760078792e-5},
            {1, -1, -.728541958464774e-3},
            {1, 1, -.187576133371704e2},
            {2, -1, .199060874071849e-2},
            {2, 1, .243574755377290e2},
            {3, -2, -.177040785499444e-3},
            {4, -2, -.259680385227130e-2},
            {4, 2, -.198704578406823e3},
            {5, -3, .738627790224287e-4},
            {5, -2, -.236264692844138e-2},
            {5, 0, -.161023121314333e1},
            {6, 3, .622322971786473e4},
            {10, -6, -.960754116701669e-8},
            {12, -8, -.510572269720488e-10},
            {12, -3, .767373781404211e-2},
            {14, -10, .663855469485254e-14},
            {14, -8, -.717590735526745e-9},
            {14, -5, .146564542926508e-4},
            {16, -10, .309029474277013e-11},
            {18, -12, -.464216300971708e-15},
            {20, -12, -.390499637961161e-13},
            {20, -10, -.236716126781431e-9},
            {24, -12, .454652854268717e-11},
            {24, -6, -.422271787482497e-2},
            {28, -12, .283911742354706e-10},
            {28, -5, .270929002720228e1}}),
        k(0.0077, 25, 680, 0.802, 0.935, 1, 1, 1, new double[][]{
            {-2, 10, -.401215699576099e9},
            {-2, 12, .484501478318406e11},
            {-1, -5, .394721471363678e-14},
            {-1, 6, .372629967374147e5},
            {0, -12, -.369794374168666e-29},
            {0, -6, -.380436407012452e-14},
            {0, -2, .475361629970233e-6},
            {0, -1, -.879148916140706e-3},
            {0, 0, .844317863844331},
            {0, 1, .122433162656600e2},
            {0, 2, -.104529634830279e3},
            {0, 3, .589702771277429e3},
            {0, 14, -.291026851164444e14},
            {1, -3, .170343072841850e-5},
            {1, -2, -.277617606975748e-3},
            {1, 0, -.344709605486686e1},
            {1, 1, .221333862447095e2},
            {1, 2, -.194646110037079e3},
            {2, -8, .808354639772825e-15},
            {2, -6, -.180845209145470e-10},
            {2, -3, -.696664158132412e-5},
            {2, -2, -.181057560300994e-2},
            {2, 0, .255830298579027e1},
            {2, 4, .328913873658481e4},
            {5, -12, -.173270241249904e-18},
            {5, -6, -.661876792558034e-6},
            {5, -3, -.395688923421250e-2},
            {6, -12, .604203299819132e-17},
            {6, -10, -.400879935920517e-13},
            {6, -8, .160751107464958e-8},
            {6, -5, .383719409025556e-4},
            {8, -12, -.649565446702457e-14},
            {10, -12, -.149095328506000e-11},
            {12, -10, .541449377329581e-8}}),
        l(0.0026, 24, 650, 0.908, 0.989, 1, 1, 4, new double[][]{
            {-12, 14, .260702058647537e10},
            {-12, 16, -.188277213604704e15},
            {-12, 18, .554923870289667e19},
            {-12, 20, -.758966946387758e23},
            {-12, 22, .413865186848908e27},
            {-10, 14, -.815038000738060e12},
            {-10, 24, -.381458260489955e33},
            {-8, 6, -.123239564600519e-1},
            {-8, 10, .226095631437174e8},
            {-8, 12, -.495017809506720e12},
            {-8, 14, .529482996422863e16},
            {-8, 18, -.444359478746295e23},
            {-8, 24, .521635864527315e35},
            {-8, 36, -.487095672740742e55},
            {-6, 8, -.714430209937547e6},
            {-5, 4, .127868634615495},
            {-5, 5, -.100752127917598e2},
            {-4, 7, .777451437960990e7},
            {-4, 16, -.108105480796471e25},
            {-3, 1, -.357578581169659e-5},
            {-3, 3, -.212857169423484e1},
            {-3, 18, .270706111085238e30},
            {-3, 20, -.695953622348829e33},
            {-2, 2, .110609027472280},
            {-2, 3, .721559163361354e2},
            {-2, 10, -.306367307532219e15},
            {-1, 0, .265839618885530e-4},
            {-1, 1, .253392392889754e-1},
            {-1, 3, -.214443041836579e3},
            {0, 0, .937846601489667},
            {0, 1, .223184043101700e1},
            {0, 2, .338401222509191e2},
            {0, 12, .494237237179718e21},
            {1, 0, -.198068404154428},
            {1, 16, -.141415349881140e31},
            {2, 1, -.993862421613651e2},
            {4, 0, .125070534142731e3},
            {5, 0, -.996473529004439e3},
            {5, 1, .473137909872765e5},
            {6, 14, .116662121219322e33},
            {10, 4, -.315874976271533e16},
            {10, 12, -.445703369196945e33},
            {14, 10, .642794932373694e33}}),
        //
        m(0.0028, 23, 650, 1.000, 0.997, 1, 0.25, 1, new double[][]{
            {0, 0, .811384363481847},
            {3, 0, -.568199310990094e4},
            {8, 0, -.178657198172556e11},
            {20, 2, .795537657613427e32},
            {1, 5, -.814568209346872e5},
            {3, 5, -.659774567602874e8},
            {4, 5, -.152861148659302e11},
            {5, 5, -.560165667510446e12},
            {1, 6, .458384828593949e6},
            {6, 6, -.385754000383848e14},
            {2, 7, .453735800004273e8},
            {4, 8, .939454935735563e12},
            {14, 8, .266572856432938e28},
            {2, 10, -.547578313899097e10},
            {5, 10, .200725701112386e15},
            {3, 12, .185007245563239e13},
            {0, 14, .185135446828337e9},
            {1, 14, -.170451090076385e12},
            {1, 18, .157890366037614e15},
            {1, 20, -.202530509748774e16},
            {28, 20, .368193926183570e60},
            {2, 22, .170215539458936e18},
            {16, 22, .639234909918741e42},
            {0, 24, -.821698160721956e15},
            {5, 24, -.795260241872306e24},
            {0, 28, .233415869478510e18},
            {3, 28, -.600079934586803e23},
            {4, 28, .594584382273384e25},
            {12, 28, .189461279349492e40},
            {16, 28, -.810093428842645e46},
            {1, 32, .188813911076809e22},
            {8, 32, .111052244098768e36},
            {14, 32, .291133958602503e46},
            {0, 36, -.329421923951460e22},
            {2, 36, -.137570282536696e26},
            {3, 36, .181508996303902e28},
            {4, 36, -.346865122768353e30},
            {8, 36, -.211961148774260e38},
            {14, 36, -.128617899887675e49},
            {24, 36, .479817895699239e65}}),
        n(0.0031, 23, 650, 0.976, 0.997, NaN, NaN, NaN, new double[][]{
            {0, -12, .280967799943151e-38},
            {3, -12, .614869006573609e-30},
            {4, -12, .582238667048942e-27},
            {6, -12, .390628369238462e-22},
            {7, -12, .821445758255119e-20},
            {10, -12, .402137961842776e-14},
            {12, -12, .651718171878301e-12},
            {14, -12, -.211773355803058e-7},
            {18, -12, .264953354380072e-2},
            {0, -10, -.135031446451331e-31},
            {3, -10, -.607246643970893e-23},
            {5, -10, -.402352115234494e-18},
            {6, -10, -.744938506925544e-16},
            {8, -10, .189917206526237e-12},
            {12, -10, .364975183508473e-5},
            {0, -8, .177274872361946e-25},
            {3, -8, -.334952758812999e-18},
            {7, -8, -.421537726098389e-8},
            {12, -8, -.391048167929649e-1},
            {2, -6, .541276911564176e-13},
            {3, -6, .705412100773699e-11},
            {4, -6, .258585887897486e-8},
            {2, -5, -.493111362030162e-10},
            {4, -5, -.158649699894543e-5},
            {7, -5, -.525037427886100},
            {4, -4, .220019901729615e-2},
            {3, -3, -.643064132636925e-2},
            {5, -3, .629154149015048e2},
            {6, -3, .135147318617061e3},
            {0, -2, .240560808321713e-6},
            {0, -1, -.890763306701305e-3},
            {3, -1, -.440209599407714e4},
            {1, 0, -.302807107747776e3},
            {0, 1, .159158748314599e4},
            {1, 1, .232534272709876e6},
            {0, 2, -.792681207132600e6},
            {1, 4, -.869871364662769e11},
            {0, 5, .354542769185671e12},
            {1, 6, .400849240129329e15}}),
        o(0.0034, 23, 650, 0.974, 0.996, 0.5, 1, 1, new double[][]{
            {0, -12, .128746023979718e-34},
            {0, -4, -.735234770382342e-11},
            {0, -1, .289078692149150e-2},
            {2, -1, .244482731907223},
            {3, -10, .141733492030985e-23},
            {4, -12, -.354533853059476e-28},
            {4, -8, -.594539202901431e-17},
            {4, -5, -.585188401782779e-8},
            {4, -4, .201377325411803e-5},
            {4, -1, .138647388209306e1},
            {5, -4, -.173959365084772e-4},
            {5, -3, .137680878349369e-2},
            {6, -8, .814897605805513e-14},
            {7, -12, .425596631351839e-25},
            {8, -10, -.387449113787755e-17},
            {8, -8, .139814747930240e-12},
            {8, -4, -.171849638951521e-2},
            {10, -12, .641890529513296e-21},
            {10, -8, .118960578072018e-10},
            {14, -12, -.155282762571611e-17},
            {14, -8, .233907907347507e-7},
            {20, -12, -.174093247766213e-12},
            {20, -10, .377682649089149e-8},
            {24, -12, -.516720236575302e-10}}),
        p(0.0041, 23, 650, 0.972, 0.997, 0.5, 1, 1, new double[][]{
            {0, -1, -.982825342010366e-4},
            {0, 0, .105145700850612e1},
            {0, 1, .116033094095084e3},
            {0, 2, .324664750281543e4},
            {1, 1, -.123592348610137e4},
            {2, -1, -.561403450013495e-1},
            {3, -3, .856677401640869e-7},
            {3, 0, .236313425393924e3},
            {4, -2, .972503292350109e-2},
            {6, -2, -.103001994531927e1},
            {7, -5, -.149653706199162e-8},
            {7, -4, -.215743778861592e-4},
            {8, -2, -.834452198291445e1},
            {10, -3, .586602660564988},
            {12, -12, .343480022104968e-25},
            {12, -6, .816256095947021e-5},
            {12, -5, .294985697916798e-2},
            {14, -10, .711730466276584e-16},
            {14, -8, .400954763806941e-9},
            {14, -3, .107766027032853e2},
            {16, -8, -.409449599138182e-6},
            {18, -8, -.729121307758902e-5},
            {20, -10, .677107970938909e-8},
            {22, -10, .602745973022975e-7},
            {24, -12, -.382323011855257e-10},
            {24, -8, .179946628317437e-2},
            {36, -12, -.345042834640005e-3}}),
        //
        q(0.0022, 23, 650, 0.848, 0.983, 1, 1, 4, new double[][]{
            {-12, 10, -.820433843259950e5},
            {-12, 12, .473271518461586e11},
            {-10, 6, -.805950021005413e-1},
            {-10, 7, .328600025435980e2},
            {-10, 8, -.356617029982490e4},
            {-10, 10, -.172985781433335e10},
            {-8, 8, .351769232729192e8},
            {-6, 6, -.775489259985144e6},
            {-5, 2, .710346691966018e-4},
            {-5, 5, .993499883820274e5},
            {-4, 3, -.642094171904570},
            {-4, 4, -.612842816820083e4},
            {-3, 3, .232808472983776e3},
            {-2, 0, -.142808220416837e-4},
            {-2, 1, -.643596060678456e-2},
            {-2, 2, -.428577227475614e1},
            {-2, 4, .225689939161918e4},
            {-1, 0, .100355651721510e-2},
            {-1, 1, .333491455143516},
            {-1, 2, .109697576888873e1},
            {0, 0, .961917379376452},
            {1, 0, -.838165632204598e-1},
            {1, 1, .247795908411492e1},
            {1, 3, -.319114969006533e4}}),
        r(0.0054, 23, 650, 0.874, 0.982, 1, 1, 1, new double[][]{
            {-8, 6, .144165955660863e-2},
            {-8, 14, -.701438599628258e13},
            {-3, -3, -.830946716459219e-16},
            {-3, 3, .261975135368109},
            {-3, 4, .393097214706245e3},
            {-3, 5, -.104334030654021e5},
            {-3, 8, .490112654154211e9},
            {0, -1, -.147104222772069e-3},
            {0, 0, .103602748043408e1},
            {0, 1, .305308890065089e1},
            {0, 5, -.399745276971264e7},
            {3, -6, .569233719593750e-11},
            {3, -2, -.464923504407778e-1},
            {8, -12, -.535400396512906e-17},
            {8, -10, .399988795693162e-12},
            {8, -8, -.536479560201811e-6},
            {8, -5, .159536722411202e-1},
            {10, -12, .270303248860217e-14},
            {10, -10, .244247453858506e-7},
            {10, -8, -.983430636716454e-5},
            {10, -6, .663513144224454e-1},
            {10, -5, -.993456957845006e1},
            {10, -4, .546491323528491e3},
            {10, -3, -.143365406393758e5},
            {10, -2, .150764974125511e6},
            {12, -12, -.337209709340105e-9},
            {14, -12, .377501980025469e-8}}),
        s(0.0022, 21, 640, 0.886, 0.990, 1, 1, 4, new double[][]{
            {-12, 20, -.532466612140254e23},
            {-12, 24, .100415480000824e32},
            {-10, 22, -.191540001821367e30},
            {-8, 14, .105618377808847e17},
            {-6, 36, .202281884477061e59},
            {-5, 8, .884585472596134e8},
            {-5, 16, .166540181638363e23},
            {-4, 6, -.313563197669111e6},
            {-4, 32, -.185662327545324e54},
            {-3, 3, -.624942093918942e-1},
            {-3, 8, -.504160724132590e10},
            {-2, 4, .187514491833092e5},
            {-1, 1, .121399979993217e-2},
            {-1, 2, .188317043049455e1},
            {-1, 3, -.167073503962060e4},
            {0, 0, .965961650599775},
            {0, 1, .294885696802488e1},
            {0, 4, -.653915627346115e5},
            {0, 28, .604012200163444e50},
            {1, 0, -.198339358557937},
            {1, 32, -.175984090163501e58},
            {3, 0, .356314881403987e1},
            {3, 1, -.575991255144384e3},
            {3, 2, .456213415338071e5},
            {4, 3, -.109174044987829e8},
            {4, 18, .437796099975134e34},
            {4, 24, -.616552611135792e46},
            {5, 4, .193568768917797e10},
            {14, 24, .950898170425042e54}}),
        t(0.0088, 20, 650, 0.803, 1.020, 1, 1, 1, new double[][]{
            {0, 0, .155287249586268e1},
            {0, 1, .664235115009031e1},
            {0, 4, -.289366236727210e4},
            {0, 12, -.385923202309848e13},
            {1, 0, -.291002915783761e1},
            {1, 10, -.829088246858083e12},
            {2, 0, .176814899675218e1},
            {2, 6, -.534686695713469e9},
            {2, 14, .160464608687834e18},
            {3, 3, .196435366560186e6},
            {3, 8, .156637427541729e13},
            {4, 0, -.178154560260006e1},
            {4, 10, -.229746237623692e16},
            {7, 3, .385659001648006e8},
            {7, 4, .110554446790543e10},
            {7, 7, -.677073830687349e14},
            {7, 20, -.327910592086523e31},
            {7, 36, -.341552040860644e51},
            {10, 10, -.527251339709047e21},
            {10, 12, .245375640937055e24},
            {10, 14, -.168776617209269e27},
            {10, 16, .358958955867578e29},
            {10, 22, -.656475280339411e36},
            {18, 18, .355286045512301e39},
            {20, 32, .569021454413270e58},
            {22, 22, -.700584546433113e48},
            {22, 36, -.705772623326374e65},
            {24, 24, .166861176200148e53},
            {28, 28, -.300475129680486e61},
            {32, 22, -.668481295196808e51},
            {32, 32, .428432338620678e69},
            {32, 36, -.444227367758304e72},
            {36, 36, -.281396013562745e77}}),
        // Auxiliary equations
        u(0.0026, 23, 650, 0.902, 0.988, 1, 1, 1, new double[][]{
            {-12, 14, .122088349258355e18},
            {-10, 10, .104216468608488e10},
            {-10, 12, -.882666931564652e16},
            {-10, 14, .259929510849499e20},
            {-8, 10, .222612779142211e15},
            {-8, 12, -.878473585050085e18},
            {-8, 14, -.314432577551552e22},
            {-6, 8, -.216934916996285e13},
            {-6, 12, .159079648196849e21},
            {-5, 4, -.339567617303423e3},
            {-5, 8, .884387651337836e13},
            {-5, 12, -.843405926846418e21},
            {-3, 2, .114178193518022e2},
            {-1, -1, -.122708229235641e-3},
            {-1, 1, -.106201671767107e3},
            {-1, 12, .903443213959313e25},
            {-1, 14, -.693996270370852e28},
            {0, -3, .648916718965575e-8},
            {0, 1, .718957567127851e4},
            {1, -2, .105581745346187e-2},
            {2, 5, -.651903203602581e15},
            {2, 10, -.160116813274676e25},
            {3, -5, -.510254294237837e-8},
            {5, -4, -.152355388953402},
            {5, 2, .677143292290144e12},
            {5, 3, .276378438378930e15},
            {6, -5, .116862983141686e-1},
            {6, 2, -.301426947980171e14},
            {8, -8, .169719813884840e-7},
            {8, 8, .104674840020929e27},
            {10, -4, -.108016904560140e5},
            {12, -12, -.990623601934295e-12},
            {12, -4, .536116483602738e7},
            {12, 4, .226145963747881e22},
            {14, -12, -.488731565776210e-9},
            {14, -10, .151001548880670e-4},
            {14, -6, -.227700464643920e5},
            {14, 6, -.781754507698846e28}}),
        v(0.0031, 23, 650, 0.960, 0.995, 1, 1, 1, new double[][]{
            {-10, -8, -.415652812061591e-54},
            {-8, -12, .177441742924043e-60},
            {-6, -12, -.357078668203377e-54},
            {-6, -3, .359252213604114e-25},
            {-6, 5, -.259123736380269e2},
            {-6, 6, .594619766193460e5},
            {-6, 8, -.624184007103158e11},
            {-6, 10, .313080299915944e17},
            {-5, 1, .105006446192036e-8},
            {-5, 2, -.192824336984852e-5},
            {-5, 6, .654144373749937e6},
            {-5, 8, .513117462865044e13},
            {-5, 10, -.697595750347391e19},
            {-5, 14, -.103977184454767e29},
            {-4, -12, .119563135540666e-47},
            {-4, -10, -.436677034051655e-41},
            {-4, -6, .926990036530639e-29},
            {-4, 10, .587793105620748e21},
            {-3, -3, .280375725094731e-17},
            {-3, 10, -.192359972440634e23},
            {-3, 12, .742705723302738e27},
            {-2, 2, -.517429682450605e2},
            {-2, 4, .820612048645469e7},
            {-1, -2, -.188214882341448e-8},
            {-1, 0, .184587261114837e-1},
            {0, -2, -.135830407782663e-5},
            {0, 6, -.723681885626348e17},
            {0, 10, -.223449194054124e27},
            {1, -12, -.111526741826431e-34},
            {1, -10, .276032601145151e-28},
            {3, 3, .134856491567853e15},
            {4, -6, .652440293345860e-9},
            {4, 3, .510655119774360e17},
            {4, 10, -.468138358908732e32},
            {5, 2, -.760667491183279e16},
            {8, -12, -.417247986986821e-18},
            {10, -2, .312545677756104e14},
            {12, -3, -.100375333864186e15},
            {14, 1, .247761392329058e27}}),
        w(0.0039, 23, 650, 0.959, 0.995, 1, 1, 4, new double[][]{
            {-12, 8, -.586219133817016e-7},
            {-12, 14, -.894460355005526e11},
            {-10, -1, .531168037519774e-30},
            {-10, 8, .109892402329239},
            {-8, 6, -.575368389425212e-1},
            {-8, 8, .228276853990249e5},
            {-8, 14, -.158548609655002e19},
            {-6, -4, .329865748576503e-27},
            {-6, -3, -.634987981190669e-24},
            {-6, 2, .615762068640611e-8},
            {-6, 8, -.961109240985747e8},
            {-5, -10, -.406274286652625e-44},
            {-4, -1, -.471103725498077e-12},
            {-4, 3, .725937724828145},
            {-3, -10, .187768525763682e-38},
            {-3, 3, -.103308436323771e4},
            {-2, 1, -.662552816342168e-1},
            {-2, 2, .579514041765710e3},
            {-1, -8, .237416732616644e-26},
            {-1, -4, .271700235739893e-14},
            {-1, 1, -.907886213483600e2},
            {0, -12, -.171242509570207e-36},
            {0, 1, .156792067854621e3},
            {1, -1, .923261357901470},
            {2, -1, -.597865988422577e1},
            {2, 2, .321988767636389e7},
            {3, -12, -.399441390042203e-29},
            {3, -5, .493429086046981e-7},
            {5, -10, .812036983370565e-19},
            {5, -8, -.207610284654137e-11},
            {5, -6, -.340821291419719e-6},
            {8, -12, .542000573372233e-17},
            {8, -10, -.856711586510214e-12},
            {10, -12, .266170454405981e-13},
            {10, -8, .858133791857099e-5}}),
        x(0.0049, 23, 650, 0.910, 0.988, 1, 1, 1, new double[][]{
            {-8, 14, .377373741298151e19},
            {-6, 10, -.507100883722913e13},
            {-5, 10, -.103363225598860e16},
            {-4, 1, .184790814320773e-5},
            {-4, 2, -.924729378390945e-3},
            {-4, 14, -.425999562292738e24},
            {-3, -2, -.462307771873973e-12},
            {-3, 12, .107319065855767e22},
            {-1, 5, .648662492280682e11},
            {0, 0, .244200600688281e1},
            {0, 4, -.851535733484258e10},
            {0, 10, .169894481433592e22},
            {1, -10, .215780222509020e-26},
            {1, -1, -.320850551367334},
            {2, 6, -.382642448458610e17},
            {3, -12, -.275386077674421e-28},
            {3, 0, -.563199253391666e6},
            {3, 8, -.326068646279314e21},
            {4, 3, .397949001553184e14},
            {5, -6, .100824008584757e-6},
            {5, -2, .162234569738433e5},
            {5, 1, -.432355225319745e11},
            {6, 1, -.592874245598610e12},
            {8, -6, .133061647281106e1},
            {8, -3, .157338197797544e7},
            {8, 1, .258189614270853e14},
            {8, 8, .262413209706358e25},
            {10, -8, -.920011937431142e-1},
            {12, -10, .220213765905426e-2},
            {12, -8, -.110433759109547e2},
            {12, -5, .847004870612087e7},
            {12, -4, -.592910695762536e9},
            {14, -12, -.183027173269660e-4},
            {14, -10, .181339603516302},
            {14, -8, -.119228759669889e4},
            {14, -6, .430867658061468e7}}),
        y(0.0031, 22, 650, 0.996, 0.994, 1, 1, 4, new double[][]{
            {0, -3, -.525597995024633e-9},
            {0, 1, .583441305228407e4},
            {0, 5, -.134778968457925e17},
            {0, 8, .118973500934212e26},
            {1, 8, -.159096490904708e27},
            {2, -4, -.315839902302021e-6},
            {2, -1, .496212197158239e3},
            {2, 4, .327777227273171e19},
            {2, 5, -.527114657850696e22},
            {3, -8, .210017506281863e-16},
            {3, 4, .705106224399834e21},
            {3, 8, -.266713136106469e31},
            {4, -6, -.145370512554562e-7},
            {4, 6, .149333917053130e28},
            {5, -2, -.149795620287641e8},
            {5, 1, -.381881906271100e16},
            {8, -8, .724660165585797e-4},
            {8, -2, -.937808169550193e14},
            {10, -5, .514411468376383e10},
            {12, -8, -.828198594040141e5}}),
        z(0.0038, 22, 650, 0.993, 0.994, 1, 1, 4, new double[][]{
            {-8, 3, .244007892290650e-10},
            {-6, 6, -.463057430331242e7},
            {-5, 6, .728803274777712e10},
            {-5, 8, .327776302858856e16},
            {-4, 5, -.110598170118409e10},
            {-4, 6, -.323899915729957e13},
            {-4, 8, .923814007023245e16},
            {-3, -2, .842250080413712e-12},
            {-3, 5, .663221436245506e12},
            {-3, 6, -.167170186672139e15},
            {-2, 2, .253749358701391e4},
            {-1, -6, -.819731559610523e-20},
            {0, 3, .328380587890663e12},
            {1, 1, -.625004791171543e8},
            {2, 6, .803197957462023e21},
            {3, -6, -.204397011338353e-10},
            {3, -2, -.378391047055938e4},
            {6, -6, .972876545938620e-2},
            {6, -5, .154355721681459e2},
            {6, -4, -.373962862928643e4},
            {6, -1, -.682859011374572e11},
            {8, -8, -.248488015614543e-3},
            {8, -4, .394536049497068e7}});

        final double nuRed, pRed, Tred, A, B, C, D, E;
        final double[][] IJn;

        SubRegion(double nu, double p, double T, double a, double b, double c, double d, double e, double[][] IJn) {

            nuRed = nu;
            pRed = p;
            Tred = T;
            A = a;
            B = b;
            C = c;
            D = d;
            E = e;
            this.IJn = IJn;
        }
    }
}
