/**
 * 
 * 날짜 포맷.
 * 
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 *     <tr bgcolor="#ccccff">
 *         <th align=left>약자</th>
 *         <th align=left>설명</th>
 *         <th align=left>리턴 타입</th>
 *         <th align=left>출력 예</th>
 *     </tr>
 *     <tr>
 *         <td><code>G</code></td>
 *         <td>Era designator</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>AD</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>y</code></td>
 *         <td>Year</td>
 *         <td><a href="#year">Year</a></td>
 *         <td><code>1996</code>; <code>96</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>M</code></td>
 *         <td>Month in year</td>
 *         <td><a href="#month">Month</a></td>
 *         <td><code>July</code>; <code>Jul</code>; <code>07</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>w</code></td>
 *         <td>Week in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>27</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>W</code></td>
 *         <td>Week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>D</code></td>
 *         <td>Day in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>189</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>d</code></td>
 *         <td>Day in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>10</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>F</code></td>
 *         <td>Day of week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>E</code></td>
 *         <td>Day in week</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>Tuesday</code>; <code>Tue</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>a</code></td>
 *         <td>Am/pm marker</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>PM</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>H</code></td>
 *         <td>Hour in day (0-23)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>k</code></td>
 *         <td>Hour in day (1-24)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>24</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>K</code></td>
 *         <td>Hour in am/pm (0-11)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>h</code></td>
 *         <td>Hour in am/pm (1-12)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>12</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>m</code></td>
 *         <td>Minute in hour</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>30</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>s</code></td>
 *         <td>Second in minute</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>55</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>S</code></td>
 *         <td>Millisecond</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>978</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#timezone">General time zone</a></td>
 *         <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>Z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#rfc822timezone">RFC 822 time zone</a></td>
 *         <td><code>-0800</code></td>
 *     </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
package kr.co.bomz.util.text;