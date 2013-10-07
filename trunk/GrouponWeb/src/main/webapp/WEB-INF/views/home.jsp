<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div class="row">
	<div class="span3 bs-docs-sidebar">
		<ul class="nav nav-list bs-docs-sidenav affix-top">
			<li class="active"><a href="#dropdowns"><i class="icon-chevron-right"></i> Dropdowns</a></li>
			<li class=""><a href="#buttonGroups"><i class="icon-chevron-right"></i> Button groups</a></li>
			<li class=""><a href="#buttonDropdowns"><i class="icon-chevron-right"></i> Button dropdowns</a></li>
			<li class=""><a href="#navs"><i class="icon-chevron-right"></i> Navs</a></li>
			<li class=""><a href="#navbar"><i class="icon-chevron-right"></i> Navbar</a></li>
			<li class=""><a href="#breadcrumbs"><i class="icon-chevron-right"></i> Breadcrumbs</a></li>
			<li class=""><a href="#pagination"><i class="icon-chevron-right"></i> Pagination</a></li>
			<li class=""><a href="#labels-badges"><i class="icon-chevron-right"></i> Labels and badges</a></li>
			<li class=""><a href="#typography"><i class="icon-chevron-right"></i> Typography</a></li>
			<li class=""><a href="#thumbnails"><i class="icon-chevron-right"></i> Thumbnails</a></li>
			<li class=""><a href="#alerts"><i class="icon-chevron-right"></i> Alerts</a></li>
			<li class=""><a href="#progress"><i class="icon-chevron-right"></i> Progress bars</a></li>
			<li class=""><a href="#media"><i class="icon-chevron-right"></i> Media object</a></li>
			<li class=""><a href="#misc"><i class="icon-chevron-right"></i> Misc</a></li>
		</ul>
	</div>
	
	<div class="span9">
		<section id="labels-badges">
          <div class="page-header">
            <h1>Labels and badges</h1>
          </div>
          <h3>Labels</h3>
          <table class="table table-bordered table-striped">
            <thead>
              <tr>
                <th>Labels</th>
                <th>Markup</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <span class="label">Default</span>
                </td>
                <td>
                  <code>&lt;span class="label"&gt;Default&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  <span class="label label-success">Success</span>
                </td>
                <td>
                  <code>&lt;span class="label label-success"&gt;Success&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  <span class="label label-warning">Warning</span>
                </td>
                <td>
                  <code>&lt;span class="label label-warning"&gt;Warning&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  <span class="label label-important">Important</span>
                </td>
                <td>
                  <code>&lt;span class="label label-important"&gt;Important&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  <span class="label label-info">Info</span>
                </td>
                <td>
                  <code>&lt;span class="label label-info"&gt;Info&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  <span class="label label-inverse">Inverse</span>
                </td>
                <td>
                  <code>&lt;span class="label label-inverse"&gt;Inverse&lt;/span&gt;</code>
                </td>
              </tr>
            </tbody>
          </table>

          <h3>Badges</h3>
          <table class="table table-bordered table-striped">
            <thead>
              <tr>
                <th>Name</th>
                <th>Example</th>
                <th>Markup</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  Default
                </td>
                <td>
                  <span class="badge">1</span>
                </td>
                <td>
                  <code>&lt;span class="badge"&gt;1&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  Success
                </td>
                <td>
                  <span class="badge badge-success">2</span>
                </td>
                <td>
                  <code>&lt;span class="badge badge-success"&gt;2&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  Warning
                </td>
                <td>
                  <span class="badge badge-warning">4</span>
                </td>
                <td>
                  <code>&lt;span class="badge badge-warning"&gt;4&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  Important
                </td>
                <td>
                  <span class="badge badge-important">6</span>
                </td>
                <td>
                  <code>&lt;span class="badge badge-important"&gt;6&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  Info
                </td>
                <td>
                  <span class="badge badge-info">8</span>
                </td>
                <td>
                  <code>&lt;span class="badge badge-info"&gt;8&lt;/span&gt;</code>
                </td>
              </tr>
              <tr>
                <td>
                  Inverse
                </td>
                <td>
                  <span class="badge badge-inverse">10</span>
                </td>
                <td>
                  <code>&lt;span class="badge badge-inverse"&gt;10&lt;/span&gt;</code>
                </td>
              </tr>
            </tbody>
          </table>

          <h3>Easily collapsible</h3>
          <p>For easy implementation, labels and badges will simply collapse (via CSS's <code>:empty</code> selector) when no content exists within.</p>

        </section>
	</div>
</div>