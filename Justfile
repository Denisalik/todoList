import 'just/docker.just'
import 'just/maven.just'
import 'just/k8s.just'
import 'just/generate.just'
import 'just/frontend.just'

default:
    @just --list

# show recipe for example 'just show lint'
show name:
    @just --show {{name}}

start: create-rsa docker

create-rsa:
    sh just/rsa.sh
