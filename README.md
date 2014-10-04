# Test Kitchen Jenkins Plugin

http://kitchen.ci

A new job type for Jenkins.  Point to a scm repo with a yml file for kitchen project, and a matrix of jobs will be created to test all instances.  A source code change to code repo will make the parent job evaluate the necessary kitchen instances again.  If any need to be added or removed they will be.  And everything remaining is built again.

Code wise, this is very similar to the Literate Plugin for Jenkins.  In other words, it's all coming from there and changing to meet my needs.