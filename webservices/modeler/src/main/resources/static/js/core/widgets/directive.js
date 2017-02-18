/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
angular
  .module('core.widgets')
  .directive('singleValue', [
    function() {
    	return {
	      restrict: 'E',
	      scope: {
	    	ngTitle: '@',
	        ngValue: '@'
	      },
	      templateUrl: 'js/core/widgets/single.value.template.html',
	      controller: ['$scope', function SingleValueController($scope) { 
	    	  $scope.bind = function(title, value) {
	    		  $scope.title = title;
	    		  $scope.value = value;
	    	  }
	      }],
	 
	      link: function(scope, iElement, iAttrs) {
	    	  scope.$watch('ngValue', function(newValue, oldValue, scope) { 
	    		  scope.bind(iAttrs.ngTitle, newValue); 
	    	  }, true);
	      }
    	}
      }
  ])
  .directive('extSvg', [ '$compile',  function ($compile) {
    return {
      restrict: 'E',
      scope: {

        /**
        * @doc property
        * @propertyOf extSvg
        * @name content
        * @description
        * Contains a SVG string.
        */
        content: '='
      },
      link: function($scope, $element) {
        $element.replaceWith($compile('<svg class="svg-content" viewBox="0 0 400 400" preserveAspectRatio="xMidYMid meet">' + $scope.content + '</svg>')($scope.$parent));
      }
    };
  }]);