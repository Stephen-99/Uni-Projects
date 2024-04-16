use warnings;
use strict;
use File::Find;                                                                 

my $numFound = 0;                                                               
my $dir = "/";
my $fileExtension = "conf";                                                        

find(\&wanted, $dir);

print "number of config files found: ${numFound}\n";

#subfunction used by find 
sub wanted 
{                                                                   
    return if ! -e;                                                             
    $numFound++ if $File::Find::name =~ /\.conf$/;                              
}
                                                                                 
                                                                                
